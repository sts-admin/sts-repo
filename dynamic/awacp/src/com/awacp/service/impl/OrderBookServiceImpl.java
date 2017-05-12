/**
 * 
 */
package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwInventory;
import com.awacp.entity.AwfInventory;
import com.awacp.entity.Contractor;
import com.awacp.entity.Factory;
import com.awacp.entity.Invoice;
import com.awacp.entity.JInventory;
import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.OrderBookInvItem;
import com.awacp.entity.ProfitSheetItem;
import com.awacp.entity.SbcInventory;
import com.awacp.entity.ShipTo;
import com.awacp.entity.SplInventory;
import com.awacp.service.InvoiceService;
import com.awacp.service.OrderBookService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class OrderBookServiceImpl extends CommonServiceImpl<OrderBook> implements OrderBookService {

	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	@Autowired
	private InvoiceService invoiceService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public OrderBook saveOrderBook(OrderBook orderBook) {
		User user = userService.getUserByUserNameOrEmail(orderBook.getUserNameOrEmail());
		if (StringUtils.isNotEmpty(orderBook.getContractorName())) { // NEW
			Contractor contractor = new Contractor();
			contractor.setCreatedByUserCode(orderBook.getCreatedByUserCode());
			contractor.setName(orderBook.getContractorName());
			contractor.setSalesPerson(user.getId());
			getEntityManager().persist(contractor);
			getEntityManager().flush();
			orderBook.setContractorId(contractor.getId());
		}

		if (StringUtils.isNotEmpty(orderBook.getShipToName())) { // NEW
			ShipTo shipTo = new ShipTo();
			shipTo.setCreatedByUserCode(orderBook.getCreatedByUserCode());
			shipTo.setShipToAddress(orderBook.getShipToName());
			getEntityManager().persist(shipTo);
			getEntityManager().flush();
			orderBook.setShipToId(shipTo.getId());
		}

		if (orderBook.getId() == null) { // New order book
			DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
			// digits
			String JOBN = new StringBuffer("JOBN").append("-").append(df.format(Calendar.getInstance().getTime()))
					.append("-").append(orderBook.getObCategory()).toString();
			orderBook.setOrderBookNumber(JOBN);
			getEntityManager().persist(orderBook);
			getEntityManager().flush();

			StringBuffer sbJOBN = new StringBuffer(orderBook.getOrderBookNumber()).append("-").append(orderBook.getId());
			if (orderBook.getSpecialInstruction() != null && !orderBook.getSpecialInstruction().isEmpty()) {
				sbJOBN.append("-").append(orderBook.getSpecialInstruction());
			}
			getEntityManager().merge(orderBook);
		} else {
			String obNumberPartial = orderBook.getOrderBookNumber()
					.substring(orderBook.getOrderBookNumber().lastIndexOf("-") + 1);
			String specInst = orderBook.getSpecialInstruction() == null
					|| orderBook.getSpecialInstruction().trim().isEmpty() ? null : orderBook.getSpecialInstruction();
			if (specInst != null && !obNumberPartial.equals(specInst)) {
				String leftPart = orderBook.getOrderBookNumber().substring(0,
						orderBook.getOrderBookNumber().lastIndexOf("-"));
				orderBook.setOrderBookNumber((leftPart.concat("-").concat(orderBook.getSpecialInstruction())));
			}
			getEntityManager().merge(orderBook);
		}

		// Update inv line item qty to reflect updated stock.
		if (orderBook.getInvItems() != null && !orderBook.getInvItems().isEmpty()) {
			for (OrderBookInvItem obItem : orderBook.getInvItems()) {
				updateInvItemQty(obItem.getBookId(), obItem.getInvItemId(), orderBook.getObCategory(),
						obItem.getOrderQty());
				if (obItem.getId() == null) { // save order book inv items
					obItem.setBookId(orderBook.getId());
					getEntityManager().persist(obItem);
				}
			}
		}
		getEntityManager().flush();

		invoiceService.addOrUpdateInvoice(orderBook.getId());
		return orderBook;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrderBook getOrderBook(Long orderBookId) {
		OrderBook ob = getEntityManager().find(OrderBook.class, orderBookId);
		ob.setShipToName(getEntityManager().find(ShipTo.class, ob.getShipToId()).getShipToAddress());
		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			Set<OrderBookInvItem> tmp = new HashSet<OrderBookInvItem>(obInvItems);
			ob.setInvItems(tmp);
		}
		return ob;
	}

	@Override
	public StsResponse<OrderBook> listOrderBooks(int pageNumber, int pageSize) {
		return initAdditionalInfo(listAll(pageNumber, pageSize, OrderBook.class.getSimpleName(), getEntityManager()));
	}

	private StsResponse<OrderBook> initAdditionalInfo(StsResponse<OrderBook> results) {
		if (results.getResults() == null)
			return null;
		for (OrderBook book : results.getResults()) {
			User user = userService.findUser(book.getSalesPersonId());
			if (user != null) {
				book.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
			if (book.getContractorId() != null && book.getContractorId() > 0) {
				book.setContractorName(getEntityManager().find(Contractor.class, book.getContractorId()).getName());
			}
			if (book.getShipToId() != null && book.getShipToId() > 0) {
				book.setShipToName(getEntityManager().find(ShipTo.class, book.getShipToId()).getShipToAddress());
			}
			if (book.getFactoryId() != null && book.getFactoryId() > 0) {
				book.setFactoryName(getEntityManager().find(Factory.class, book.getFactoryId()).getFactoryName());
			}

		}
		return results;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		OrderBook entity = getOrderBook(id);
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
	@Transactional
	public int updateInvItemQty(Long bookId, Long lineItemId, String invName, int orderQty) {
		invName = invName.trim();
		List<OrderBookInvItem> obii = getEntityManager().createNamedQuery("OrderBookInvItem.getByAllBookAndInvItemId")
				.setParameter("bookId", bookId).setParameter("invItemId", lineItemId).getResultList();
		int exQty = obii != null && !obii.isEmpty() ? obii.get(0).getOrderQty() : 0;
		int diff = orderQty - exQty;
		System.err.println("orderQty = " + orderQty + ", exQty = " + exQty + ", diff = " + diff);
		if (invName.equalsIgnoreCase("j")) {
			JInventory jInv = getEntityManager().find(JInventory.class, lineItemId);
			jInv.setQuantity(jInv.getQuantity() - (diff));
			getEntityManager().merge(jInv);
		} else if (invName.equalsIgnoreCase("aw")) {
			AwInventory awInv = getEntityManager().find(AwInventory.class, lineItemId);
			awInv.setQuantity(awInv.getQuantity() - (diff));
			getEntityManager().merge(awInv);
		} else if (invName.equalsIgnoreCase("awf")) {
			AwfInventory awf = getEntityManager().find(AwfInventory.class, lineItemId);
			awf.setQuantity(awf.getQuantity() - (diff));
			getEntityManager().merge(awf);
		} else if (invName.equalsIgnoreCase("sbc")) {
			SbcInventory sbcInv = getEntityManager().find(SbcInventory.class, lineItemId);
			sbcInv.setQuantity(sbcInv.getQuantity() - (diff));
			getEntityManager().merge(sbcInv);
		} else if (invName.equalsIgnoreCase("spl")) {
			SplInventory splInv = getEntityManager().find(SplInventory.class, lineItemId);
			splInv.setQuantity(splInv.getQuantity() - (diff));
			getEntityManager().merge(splInv);
		}
		getEntityManager().flush();
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderBook> listByJobOrder(Long jobOrderId) {
		return getEntityManager().createNamedQuery("OrderBook.getByJobOrderId").setParameter("jobId", jobOrderId)
				.getResultList();
	}

	@Override
	public Double getPrice(Long lineItemId, String invName) {
		invName = invName.trim();
		if (invName.equalsIgnoreCase("j")) {
			return getEntityManager().find(JInventory.class, lineItemId).getBillableCost();
		} else if (invName.equalsIgnoreCase("aw")) {
			return getEntityManager().find(AwInventory.class, lineItemId).getBillableCost();
		} else if (invName.equalsIgnoreCase("awf")) {
			return getEntityManager().find(AwfInventory.class, lineItemId).getBillableCost();
		} else if (invName.equalsIgnoreCase("sbc")) {
			return getEntityManager().find(SbcInventory.class, lineItemId).getBillableCost();
		} else if (invName.equalsIgnoreCase("spl")) {
			return getEntityManager().find(SplInventory.class, lineItemId).getBillableCost();
		}
		return 0.0D;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String cancelOrderBook(Long orderBookId) {
		System.err.println("cancelOrderBook: orderBookId: " + orderBookId);
		OrderBook ob = getOrderBook(orderBookId);
		List<ProfitSheetItem> psItems = getEntityManager().createNamedQuery("ProfitSheetItem.getByOrderBookId")
				.setParameter("orderBookId", orderBookId).getResultList();
		// Archive profit sheet items for this order book
		if (psItems != null && !psItems.isEmpty()) {
			System.err.println("cancelOrderBook: psItems size = " + psItems.size());
			for (ProfitSheetItem psItem : psItems) {
				psItem.setArchived(true);
				getEntityManager().merge(psItem);
				getEntityManager().flush();
			}
		} else {
			System.err.println("cancelOrderBook: psItems is null or empty");
		}
		// Archive inventory items for this order book and update respective
		// inventory
		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			System.err.println("cancelOrderBook: obInvItems size = " + obInvItems.size());
			for (OrderBookInvItem obInvItem : obInvItems) {
				obInvItem.setArchived(true);
				getEntityManager().merge(obInvItem);
				updateInvItemQty(obInvItem.getBookId(), obInvItem.getInvItemId(), ob.getObCategory(), 0);
			}
		} else {
			System.err.println("cancelOrderBook: obInvItems is null or empty");
		}
		// Finally archive this order
		ob.setCancelled(true);
		getEntityManager().merge(ob);
		getEntityManager().flush();

		int allCount = 0, cancelledCount = 0;
		// Archive the invoice if all order books of this order are cancelled.
		Object object = getEntityManager().createNamedQuery("OrderBook.getCountByJobOrderId")
				.setParameter("jobId", ob.getJobId()).getSingleResult();
		if (object != null) {
			allCount = (((Long) object).intValue());
		}
		Object object2 = getEntityManager().createNamedQuery("OrderBook.getCancelledCountByJobOrderId")
				.setParameter("jobId", ob.getJobId()).getSingleResult();
		if (object2 != null) {
			cancelledCount = (((Long) object2).intValue());
		}
		System.err.println("all order book count = " + allCount + ", cancelled order book count = " + cancelledCount);
		// No open order books, cancel invoice.
		JobOrder jobOrder = getEntityManager().find(JobOrder.class, ob.getJobId());
		if (allCount == cancelledCount) {
			System.err.println("total booking for this job with id " + ob.getJobId()
					+ " are cancelled so archive this invoice attached to the job");
			Invoice invoice = getEntityManager().find(Invoice.class, jobOrder.getInvoiceId());
			invoice.setArchived(true);
			getEntityManager().merge(invoice);
			getEntityManager().flush();
		}

		List<Invoice> orderInvoices = getEntityManager().createNamedQuery("Invoice.getByOrderId")
				.setParameter("jobOrderId", ob.getJobId()).getResultList();
		// detach invoices from job order if they are archived or cancelled.
		if (orderInvoices == null || orderInvoices.isEmpty()) {
			System.err.println("all invoices archived, detach it from job order");
			jobOrder.setInvoiceId(null);
			getEntityManager().merge(jobOrder);
			getEntityManager().flush();
		}
		return "success";
	}

	public static void main(String args[]) {
		String num = "abc-def-A";
		String partial = "A";
		if (num.substring(num.lastIndexOf("-") + 1).equalsIgnoreCase(partial)) {
			System.err.println("Yes");
		} else {
			System.err.println("No");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String uncancellOrderBook(Long orderBookId) {
		System.err.println("uncancellOrderBook: orderBookId = " + orderBookId);
		OrderBook ob = getOrderBook(orderBookId);
		List<ProfitSheetItem> psItems = getEntityManager().createNamedQuery("ProfitSheetItem.getAllByOrderBookId")
				.setParameter("orderBookId", orderBookId).getResultList();
		// Archive profit sheet items for this order book
		if (psItems != null && !psItems.isEmpty()) {
			System.err.println("uncancellOrderBook: psItems  size " + psItems.size());
			for (ProfitSheetItem psItem : psItems) {
				psItem.setArchived(false);
				getEntityManager().merge(psItem);
			}
		} else {
			System.err.println("uncancellOrderBook: psItems  is null or empty");
		}
		getEntityManager().flush();
		// Archive inventory items for this order book and update respective
		// inventory
		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getAllByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			System.err.println("uncancellOrderBook: obInvItems  size " + obInvItems.size());
			for (OrderBookInvItem obInvItem : obInvItems) {
				obInvItem.setArchived(false);
				getEntityManager().merge(obInvItem);
				getEntityManager().flush();
				updateInvItemQty(obInvItem.getBookId(), obInvItem.getInvItemId(), ob.getObCategory(),
						(obInvItem.getOrderQty() * 2));
			}
		} else {
			System.err.println("uncancellOrderBook: obInvItems  is null or empty");
		}
		// Finally archive this order

		ob.setCancelled(false);
		getEntityManager().merge(ob);
		getEntityManager().flush();

		List<Invoice> invoices = getEntityManager().createNamedQuery("Invoice.getArchivedInvoiceByOrderId")
				.setParameter("jobOrderId", ob.getJobId()).getResultList();
		if (invoices != null && !invoices.isEmpty()) {
			Invoice invoice = invoices.get(0);
			invoice.setArchived(false);
			getEntityManager().merge(invoice);
			getEntityManager().flush();
			JobOrder jo = getEntityManager().find(JobOrder.class, invoices.get(0).getJobOrderId());
			if (jo != null) {
				jo.setInvoiceId(invoice.getId());
				getEntityManager().merge(jo);
				getEntityManager().flush();
			}
		}

		return "success";
	}
}
