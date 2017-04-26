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
import com.awacp.service.OrderBookService;

public class InvoiceServiceImpl implements InvoiceService {
	private EntityManager entityManager;

	@Autowired
	OrderBookService obService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Invoice getInvoice(Long invoiceId) {
		Invoice invoice = getEntityManager().find(Invoice.class, invoiceId);
		invoice.setProfitOrLossLabel("Profit");
		if (invoice.getTotalProfit() == null) {
			invoice.setTotalProfit(0.0D);
		}
		if (invoice.getTotalProfit() < 0) {
			invoice.setProfitOrLossLabel("Loss");
		}
		return invoice;
	}

	@Override
	@Transactional
	public Invoice saveInvoice(Invoice invoice) {
		Double totalCost = 0.0D;
		if (invoice.getProfitSheetItems() != null && !invoice.getProfitSheetItems().isEmpty()) {
			for (ProfitSheetItem psi : invoice.getProfitSheetItems()) {
				System.err.println("PO# : " + psi.getAwacpPoNumber() + ", FAC INV: " + psi.getFacInv() + ", INV AMT: "
						+ psi.getInvAmount() + ", ORBF: " + psi.getDateUpdated() + ", FREIGHT: " + psi.getFreight());
				totalCost = totalCost + (psi.getInvAmount() == null ? 0.0D : psi.getInvAmount())
						+ (psi.getFreight() == null ? 0.0D : psi.getFreight());
			}
		} else {
			if (invoice.getProfitSheetItems() != null) {
				invoice.getProfitSheetItems().clear();
			}
		}

		invoice.setTotalCost(totalCost);
		Double billableAmount = getEntityManager().find(JobOrder.class, invoice.getJobOrderId()).getBillingAmount();
		if (billableAmount == null) {
			billableAmount = 0.0D;
		}
		invoice.setBillableAmount(billableAmount);
		invoice.setBalancePayable(billableAmount);
		invoice.setTotalProfit(billableAmount - totalCost);
		invoice.setProfitPercent((((billableAmount - totalCost) / totalCost) * 100));
		if (invoice.getPartialPayment() == null) {
			invoice.setPartialPayment(0.0D);
		}
		if(invoice.getBalancePayable() == null){
			invoice.setBalancePayable(0.0D);
		}
		if (billableAmount == invoice.getPartialPayment() || invoice.getBalancePayable() <= 0) {
			invoice.setShipment("FULL");
		}
		if (invoice.getId() != null && invoice.getId() > 0) {
			getEntityManager().merge(invoice);
		} else {
			getEntityManager().persist(invoice);
			getEntityManager().flush();
			JobOrder jobOrder = getEntityManager().find(JobOrder.class, invoice.getJobOrderId());
			jobOrder.setInvoiceId(invoice.getId());
			getEntityManager().merge(jobOrder);
		}
		getEntityManager().flush();

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
	public Invoice addDummyInvoice(Long orderBookId) {
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
			invoice.setShipment("FULL");
			invoice.setJobOrderId(ob.getJobId());
			invoice.setAwOrderNumber(ob.getJobOrderNumber());
		}

		if (!ob.getObCategory().equalsIgnoreCase("regular")
				&& (ob.getInvItems() != null && !ob.getInvItems().isEmpty())) {
			Set<ProfitSheetItem> psItems = new HashSet<ProfitSheetItem>();
			ProfitSheetItem psi = new ProfitSheetItem();
			psi.setAwacpPoNumber(ob.getOrderBookNumber());
			psi.setOrbf(ob.getOrbf());
			psi.setInvAmount(0.0D);
			Double amount = 0.0D;
			for (OrderBookInvItem obInvItem : ob.getInvItems()) {
				amount = (amount
						+ (obInvItem.getOrderQty() * obService.getPrice(obInvItem.getInvItemId(), ob.getObCategory())));
			}
			psi.setInvAmount(amount);
			psItems.add(psi);

			if (existingInvoice) {
				invoice.getProfitSheetItems().add(psi);
			} else {
				invoice.setProfitSheetItems(psItems);
			}
		}

		return saveInvoice(invoice);
	}
	/*
	 * public static void main(String args[]){ Double cost = 200D; Double bill =
	 * 15.50D; if(cost > bill){ System.err.println("Loss is: "+ (bill - cost));
	 * }else{ System.err.println("Profit is: "+ (bill - cost)); } Double percent
	 * = (((bill - cost) / cost) * 100) ; if(percent < 0){
	 * System.err.println("Loss % = "+ percent); }else{
	 * System.err.println("Profit % = "+ percent); }
	 * 
	 * }
	 */
}
