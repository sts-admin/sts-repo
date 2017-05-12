package com.awacp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Invoice;
import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.OrderBookInvItem;
import com.awacp.entity.ProfitSheetItem;
import com.awacp.service.InvoiceService;
import com.awacp.service.JobService;
import com.awacp.service.OrderBookService;
import com.sts.core.constant.StsCoreConstant;

public class InvoiceServiceImpl implements InvoiceService {
	private EntityManager entityManager;

	@Autowired
	OrderBookService obService;

	@Autowired
	JobService jobService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	private void setInvoiceProfitSheet(Invoice invoice) {
		// Set invoice profit sheet line items
		List<ProfitSheetItem> psiItems = getEntityManager().createNamedQuery("ProfitSheetItem.getAllByInvoiceId")
				.setParameter("invoiceId", invoice.getId()).getResultList();
		if (psiItems != null && !psiItems.isEmpty()) {
			Set<ProfitSheetItem> psiItemsSet = new HashSet<ProfitSheetItem>(psiItems);
			invoice.setProfitSheetItems(psiItemsSet);
		}
	}

	@Override
	public Invoice getInvoice(Long invoiceId) {
		Invoice invoice = getEntityManager().find(Invoice.class, invoiceId);
		invoice.setProfitOrLossLabel("Profit");
		setInvoiceProfitSheet(invoice);
		return invoice;
	}

	private Double calculateTotalCost(Set<ProfitSheetItem> psItems) {
		Double totalCost = 0.0D;

		if (psItems != null && !psItems.isEmpty()) {
			for (ProfitSheetItem psi : psItems) {
				totalCost = totalCost + (psi.getInvAmount() == null ? 0.0D : psi.getInvAmount())
						+ (psi.getFreight() == null ? 0.0D : psi.getFreight());
			}
		}
		return totalCost;
	}

	@Override
	@Transactional
	public Invoice saveInvoice(Invoice invoice) {
		Double billableAmount = jobService.getJobOrder(invoice.getJobOrderId()).getBillingAmount();
		invoice.setBillableAmount(billableAmount == null ? 0.0D : billableAmount);

		/**
		 * Check to not to loose total cost, profit and percent values when
		 * update call to invoice is made from order book update process,
		 * because no profit sheet items available in this process process
		 **/
		if (invoice.getProfitSheetItems() != null && !invoice.getProfitSheetItems().isEmpty()) {
			Double totalCost = calculateTotalCost(invoice.getProfitSheetItems());
			invoice.setTotalCost(totalCost);
			invoice.setTotalProfit(billableAmount - totalCost);
			invoice.setProfitPercent((((billableAmount - totalCost) / totalCost) * 100));
		}

		if (invoice.getPartialPayment() == null) {
			invoice.setPartialPayment(0.0D);
		}
		if (invoice.getPrePayAmount() == null) {
			invoice.setPrePayAmount(0.0D);
		}
		if (invoice.getTotalProfit() < 0) {
			invoice.setProfitOrLossLabel("Loss");
		}
		if (invoice.getId() != null && invoice.getId() > 0) { // update invoice
			Invoice exInvoice = getInvoice(invoice.getId());
			Double exTotalPayment = exInvoice.getTotalPayment();
			int cResult = exInvoice.getPrePayAmount().compareTo(invoice.getPrePayAmount());
			// Advance payment amount changed,
			// re-calculate total payment
			if (cResult < 0 || cResult > 1) {
				exTotalPayment = (exTotalPayment - (invoice.getPrePayAmount() < exInvoice.getPrePayAmount()
						? invoice.getPrePayAmount() : -(invoice.getPrePayAmount() - exInvoice.getPrePayAmount())));

			}
			// & partial payment amount given, add it to calculation.
			if (invoice.getPartialPayment() > 0) {
				exTotalPayment += invoice.getPartialPayment();
			}

			invoice.setTotalPayment(exTotalPayment);
		}
		invoice.setPartialPayment(0.0D);

		if (invoice.getBillableAmount().intValue() > 0
				&& invoice.getBillableAmount().compareTo(invoice.getTotalPayment()) <= 0) {
			invoice.setShipment(StsCoreConstant.INV_SHIPMENT_TYPE_FULL);
		}else{
			invoice.setShipment(StsCoreConstant.INV_SHIPMENT_TYPE_PARTIAL);
		}

		JobOrder jobOrder = getEntityManager().find(JobOrder.class, invoice.getJobOrderId());
		if (invoice.getId() == null || invoice.getId() <= 0) {
			getEntityManager().persist(invoice);
			getEntityManager().flush();
			jobOrder.setInvoiceId(invoice.getId());
		} else {
			getEntityManager().merge(invoice);
		}
		if (invoice.getShipment().equalsIgnoreCase(StsCoreConstant.INV_SHIPMENT_TYPE_PARTIAL)) {
			jobOrder.setInvoiceMode(StsCoreConstant.INV_MODE_PTL);
		} else if (invoice.getShipment().equalsIgnoreCase(StsCoreConstant.INV_SHIPMENT_TYPE_FULL)) {
			jobOrder.setInvoiceMode(StsCoreConstant.INV_MODE_BILL);
		}
		getEntityManager().merge(jobOrder);

		if (invoice.getProfitSheetItems() != null && !invoice.getProfitSheetItems().isEmpty()) {
			for (ProfitSheetItem psi : invoice.getProfitSheetItems()) {
				if (psi.getId() == null) {
					psi.setInvoiceId(invoice.getId());
				}
				saveProfitSheetItem(psi);
			}
		}

		return invoice;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Invoice entity = getInvoice(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice getInvoiceByJobOrder(Long jobOrderId) {
		List<Invoice> invoices = getEntityManager().createNamedQuery("Invoice.getByOrderId")
				.setParameter("jobOrderId", jobOrderId).getResultList();
		return invoices == null || invoices.isEmpty() ? null : invoices.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Invoice addOrUpdateInvoice(Long orderBookId) {
		OrderBook ob = obService.getOrderBook(orderBookId);
		List<Invoice> invoices = getEntityManager().createNamedQuery("Invoice.getByOrderId")
				.setParameter("jobOrderId", ob.getJobId()).getResultList();
		Invoice invoice = null;
		boolean existingInvoice = false;
		if (invoices != null && !invoices.isEmpty()) {
			invoice = invoices.get(0);
			existingInvoice = true;
		} else {
			invoice = new Invoice();
			invoice.setShipment(StsCoreConstant.INV_SHIPMENT_TYPE_PARTIAL);
			invoice.setJobOrderId(ob.getJobId());
			invoice.setAwOrderNumber(ob.getJobOrderNumber());
			invoice.setPrePayAmount(0.0D);
			invoice.setPartialPayment(0.0D);
		}
		invoice.setShipTo(ob.getShipToName());
		if (existingInvoice) { // Exist i.e update case
			List<ProfitSheetItem> exPsItems = getEntityManager()
					.createNamedQuery("ProfitSheetItem.getNonManualItemByInvoiceAndOrderId")
					.setParameter("orderBookId", orderBookId).setParameter("invoiceId", invoice.getId())
					.getResultList();
			if (exPsItems != null && !exPsItems.isEmpty()) {
				exPsItems.get(0).setOrbf(ob.getOrbf());
				Set<ProfitSheetItem> psItems = new HashSet<ProfitSheetItem>();
				psItems.add(exPsItems.get(0));
				invoice.setProfitSheetItems(psItems);
			}
		} else { // New i.e save case
			if (!ob.getObCategory().equalsIgnoreCase("regular")
					&& (ob.getInvItems() != null && !ob.getInvItems().isEmpty())) {
				Set<ProfitSheetItem> psItems = new HashSet<ProfitSheetItem>();
				ProfitSheetItem psi = new ProfitSheetItem();
				psi.setAwacpPoNumber(ob.getOrderBookNumber());
				psi.setOrbf(ob.getOrbf());
				psi.setInvAmount(0.0D);
				psi.setInvAmount(calculateInvoiceAmount(ob.getInvItems(), ob.getObCategory()));
				psi.setObCategory(ob.getObCategory());
				psi.setOrderBookId(orderBookId);
				psItems.add(psi);
				invoice.setProfitSheetItems(psItems);
			}
		}

		return saveInvoice(invoice);
	}

	private Double calculateInvoiceAmount(Set<OrderBookInvItem> invItems, String obCategory) {
		Double amt = 0.0D;
		for (OrderBookInvItem obInvItem : invItems) {
			amt = (amt + (obInvItem.getOrderQty() * obService.getPrice(obInvItem.getInvItemId(), obCategory)));
		}
		return amt;
	}

	@Override
	@Transactional
	public Long saveProfitSheetItem(ProfitSheetItem psi) {
		System.err.println("PO# : " + psi.getAwacpPoNumber() + ", FAC INV: " + psi.getFacInv() + ", INV AMT: "
				+ psi.getInvAmount() + ", ORBF: " + psi.getDateUpdated() + ", FREIGHT: " + psi.getFreight());
		if (psi.getId() == null) {
			getEntityManager().persist(psi);
		} else {
			ProfitSheetItem psItem = getEntityManager().find(ProfitSheetItem.class, psi.getId());
			psItem.setComment(psi.getComment());
			psItem.setFacInv(psi.getFacInv());
			psItem.setFreight(psi.getFreight());
			psItem.setInvAmount(psi.getInvAmount());
			getEntityManager().merge(psi);
		}
		getEntityManager().flush();
		return psi.getId();
	}

	public static void main(String args[]) {
		Double exAmount = 100D;
		Double parPay = 20D;
		Double total = exAmount + parPay;

		System.err.println("total before = " + total);

		Double currAmount = 110D;
		total = (total - (currAmount < exAmount ? currAmount : -(currAmount - exAmount)));

		System.err.println("total after = " + total);

	}
}
