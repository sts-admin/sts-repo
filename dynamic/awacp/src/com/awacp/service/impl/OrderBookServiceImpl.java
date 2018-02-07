/**
 * 
 */
package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwInventory;
import com.awacp.entity.AwfInventory;
import com.awacp.entity.ClaimFollowup;
import com.awacp.entity.Contractor;
import com.awacp.entity.Factory;
import com.awacp.entity.Invoice;
import com.awacp.entity.JInventory;
import com.awacp.entity.JobOrder;
import com.awacp.entity.Orbf;
import com.awacp.entity.OrderBook;
import com.awacp.entity.OrderBookInvItem;
import com.awacp.entity.ProfitSheetItem;
import com.awacp.entity.SbcInventory;
import com.awacp.entity.ShipTo;
import com.awacp.entity.ShipmentStatus;
import com.awacp.entity.SplInventory;
import com.awacp.service.InvoiceService;
import com.awacp.service.OrderBookService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.service.FileService;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class OrderBookServiceImpl extends CommonServiceImpl<OrderBook> implements OrderBookService {

	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private FileService fileService;

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
		if (orderBook.getFactoryId() != null && orderBook.getFactoryId() > 0) {
			orderBook.setFactoryType("regular");
		} else {
			orderBook.setFactoryType(orderBook.getObCategory());
		}

		if (orderBook.getId() == null) { // New order book
			DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
			// digits
			String JOBN = new StringBuffer("JOBN").append("-").append(df.format(Calendar.getInstance().getTime()))
					.append("-").append(orderBook.getObCategory()).toString();
			orderBook.setOrderBookNumber(JOBN);
			getEntityManager().persist(orderBook);
			getEntityManager().flush();

			StringBuffer sbJOBN = new StringBuffer(orderBook.getOrderBookNumber()).append("-")
					.append(orderBook.getId());
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
					obItem.setInvType(orderBook.getObCategory());
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
		if (ob.getContractorId() != null) {
			ob.setContractorName(getEntityManager().find(Contractor.class, ob.getContractorId()).getName());
		}
		if (ob.getShipToId() != null) {
			ob.setShipToName(getEntityManager().find(ShipTo.class, ob.getShipToId()).getShipToAddress());
		}

		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			Set<OrderBookInvItem> tmp = new HashSet<OrderBookInvItem>(obInvItems);
			ob.setInvItems(tmp);
		}
		ob.setDeliveryStatuses(getShipmentStatus(ob.getId()));
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

			book.setObADocCount(fileService.getFileCount(StsCoreConstant.DOC_OB_A_DOC, book.getId()));
			book.setObYXlsDocCount(fileService.getFileCount(StsCoreConstant.DOC_OB_Y_XLS, book.getId()));
			book.setObAckPdfDocCount(fileService.getFileCount(StsCoreConstant.DOC_OB_ACK_PDF, book.getId()));
			book.setObFrtPdfDocCount(fileService.getFileCount(StsCoreConstant.DOC_OB_FRT_PDF, book.getId()));
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
		OrderBook ob = getOrderBook(orderBookId);
		List<ProfitSheetItem> psItems = getEntityManager().createNamedQuery("ProfitSheetItem.getByOrderBookId")
				.setParameter("orderBookId", orderBookId).getResultList();
		// Archive profit sheet items for this order book
		if (psItems != null && !psItems.isEmpty()) {
			for (ProfitSheetItem psItem : psItems) {
				psItem.setArchived(true);
				getEntityManager().merge(psItem);
				getEntityManager().flush();
			}
		}
		// Archive inventory items for this order book and update respective
		// inventory
		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			for (OrderBookInvItem obInvItem : obInvItems) {
				obInvItem.setArchived(true);
				getEntityManager().merge(obInvItem);
				updateInvItemQty(obInvItem.getBookId(), obInvItem.getInvItemId(), ob.getObCategory(), 0);
			}
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

		// No open order books, cancel invoice.
		JobOrder jobOrder = getEntityManager().find(JobOrder.class, ob.getJobId());
		if (allCount == cancelledCount) {
			Invoice invoice = getEntityManager().find(Invoice.class, jobOrder.getInvoiceId());
			invoice.setArchived(true);
			getEntityManager().merge(invoice);
			getEntityManager().flush();
		}

		List<Invoice> orderInvoices = getEntityManager().createNamedQuery("Invoice.getByOrderId")
				.setParameter("jobOrderId", ob.getJobId()).getResultList();
		// detach invoices from job order if they are archived or cancelled.
		if (orderInvoices == null || orderInvoices.isEmpty()) {
			jobOrder.setInvoiceId(null);
			getEntityManager().merge(jobOrder);
			getEntityManager().flush();
		}
		return "success";
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String uncancellOrderBook(Long orderBookId) {
		OrderBook ob = getOrderBook(orderBookId);
		List<ProfitSheetItem> psItems = getEntityManager().createNamedQuery("ProfitSheetItem.getAllByOrderBookId")
				.setParameter("orderBookId", orderBookId).getResultList();
		// Archive profit sheet items for this order book
		if (psItems != null && !psItems.isEmpty()) {
			for (ProfitSheetItem psItem : psItems) {
				psItem.setArchived(false);
				getEntityManager().merge(psItem);
			}
		}
		getEntityManager().flush();
		// Archive inventory items for this order book and update respective
		// inventory
		List<OrderBookInvItem> obInvItems = getEntityManager().createNamedQuery("OrderBookInvItem.getAllByOrderBookId")
				.setParameter("bookId", orderBookId).getResultList();
		if (obInvItems != null && !obInvItems.isEmpty()) {
			for (OrderBookInvItem obInvItem : obInvItems) {
				obInvItem.setArchived(false);
				getEntityManager().merge(obInvItem);
				getEntityManager().flush();
				updateInvItemQty(obInvItem.getBookId(), obInvItem.getInvItemId(), ob.getObCategory(),
						(obInvItem.getOrderQty() * 2));
			}
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

	@Override
	public StsResponse<OrderBook> generateOrderBookReport(OrderBook orderBook) {
		StringBuffer sb = new StringBuffer("SELECT t FROM ").append(OrderBook.class.getSimpleName())
				.append(" t WHERE t.archived ='false'");

		StringBuffer countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(OrderBook.class.getSimpleName())
				.append(" t WHERE t.archived ='false'");

		if (orderBook.getFromDate() != null && orderBook.getToDate() != null) {
			sb.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
			countQuery
					.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
		}

		if (orderBook.getYear() > 0) {
			sb.append(" AND FUNC('YEAR', t.dateCreated) =:year");
			countQuery.append(" AND FUNC('YEAR', t.dateCreated) =:year");
		}
		if (orderBook.getCreatedByUserCode() != null && !orderBook.getCreatedByUserCode().isEmpty()) {
			sb.append(" AND t.createdByUserCode =:createdByUserCode");
			countQuery.append(" AND t.createdByUserCode =:createdByUserCode");
		}
		if (orderBook.getSalesPersonId() != null && orderBook.getSalesPersonId() > 0) {
			sb.append(" AND t.salesPersonId =:salesPersonId");
			countQuery.append(" AND t.salesPersonId =:salesPersonId");
		}
		if (orderBook.getContractorId() != null && orderBook.getContractorId() > 0) {
			sb.append(" AND t.contractorId =:contractorId");
			countQuery.append(" AND t.contractorId =:contractorId");
		}
		if (orderBook.getFactoryType() != null && !orderBook.getFactoryType().isEmpty()) {
			sb.append(" AND LOWER(t.factoryType) =:factoryType");
			countQuery.append(" AND LOWER(t.factoryType) =:factoryType");
		}
		if (orderBook.getShipToId() != null && orderBook.getShipToId() > 0) {
			sb.append(" AND t.shipToId =:shipToId");
			countQuery.append(" AND t.shipToId =:shipToId");
		}
		/**
		 * Search fields
		 */
		if (orderBook.getOrderBookNumber() != null && !orderBook.getOrderBookNumber().isEmpty()) {
			sb.append(" AND t.orderBookNumber =:orderBookNumber");
			countQuery.append(" AND t.orderBookNumber =:orderBookNumber");
		}
		if (orderBook.getJobName() != null && !orderBook.getJobName().isEmpty()) {
			sb.append(" AND LOWER(t.jobName) LIKE :jobName");
			countQuery.append(" AND LOWER(t.jobName) LIKE :jobName");
		}
		if (orderBook.getJobAddress() != null && !orderBook.getJobAddress().isEmpty()) {
			sb.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
			countQuery.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
		}
		if (orderBook.getAttn() != null && !orderBook.getAttn().isEmpty()) {
			sb.append(" AND t.attn =:attn");
			countQuery.append(" AND t.attn =:attn");
		}
		if (orderBook.getOrbf() != null && !orderBook.getOrbf().isEmpty()) {
			sb.append(" AND t.orbf =:orbf");
			countQuery.append(" AND t.orbf =:orbf");
		}
		if (orderBook.getJobOrderNumber() != null && !orderBook.getJobOrderNumber().isEmpty()) {
			sb.append(" AND t.jobOrderNumber =:jobOrderNumber");
			countQuery.append(" AND t.jobOrderNumber =:jobOrderNumber");
		}
		if (orderBook.getComment() != null && !orderBook.getComment().isEmpty()) {
			sb.append(" AND LOWER(t.comment) LIKE :comment");
			countQuery.append(" AND LOWER(t.comment) LIKE :comment");
		}
		if (orderBook.getEstDate() != null) {
			sb.append(" AND FUNC('DATE', t.estDate) = :estDate");
			countQuery.append(" AND FUNC('DATE', t.estDate) = :estDate");
		}
		/**
		 * Search fields
		 */

		Query query = getEntityManager().createQuery(sb.toString());
		Query query2 = getEntityManager().createQuery(countQuery.toString());

		if (orderBook.getFromDate() != null && orderBook.getToDate() != null) {
			query.setParameter("fromDate", orderBook.getFromDate().getTime(), TemporalType.DATE);
			query.setParameter("toDate", orderBook.getToDate().getTime(), TemporalType.DATE);

			query2.setParameter("fromDate", orderBook.getFromDate().getTime(), TemporalType.DATE);
			query2.setParameter("toDate", orderBook.getToDate().getTime(), TemporalType.DATE);
		}

		if (orderBook.getYear() > 0) {
			query.setParameter("year", orderBook.getYear());
			query2.setParameter("year", orderBook.getYear());
		}
		if (orderBook.getCreatedByUserCode() != null && !orderBook.getCreatedByUserCode().isEmpty()) {
			query.setParameter("createdByUserCode", orderBook.getCreatedByUserCode());
			query2.setParameter("createdByUserCode", orderBook.getCreatedByUserCode());
		}
		if (orderBook.getSalesPersonId() != null && orderBook.getSalesPersonId() > 0) {
			query.setParameter("salesPersonId", orderBook.getSalesPersonId());
			query2.setParameter("salesPersonId", orderBook.getSalesPersonId());
		}
		if (orderBook.getContractorId() != null && orderBook.getContractorId() > 0) {
			query.setParameter("contractorId", orderBook.getContractorId());
			query2.setParameter("contractorId", orderBook.getContractorId());
		}

		if (orderBook.getFactoryType() != null && !orderBook.getFactoryType().isEmpty()) {
			query.setParameter("factoryType", orderBook.getFactoryType().toLowerCase());
			query2.setParameter("factoryType", orderBook.getFactoryType().toLowerCase());
		}

		if (orderBook.getShipToId() != null && orderBook.getShipToId() > 0) {
			query.setParameter("shipToId", orderBook.getShipToId());
			query2.setParameter("shipToId", orderBook.getShipToId());
		}

		/**
		 * Search fields
		 */
		if (orderBook.getOrderBookNumber() != null && !orderBook.getOrderBookNumber().isEmpty()) {
			query.setParameter("orderBookNumber", orderBook.getOrderBookNumber());
			query2.setParameter("orderBookNumber", orderBook.getOrderBookNumber());
		}
		if (orderBook.getJobName() != null && !orderBook.getJobName().isEmpty()) {
			query.setParameter("jobName", "%" + orderBook.getJobName().toLowerCase() + "%");
			query2.setParameter("jobName", "%" + orderBook.getJobName().toLowerCase() + "%");
		}
		if (orderBook.getJobAddress() != null && !orderBook.getJobAddress().isEmpty()) {
			query.setParameter("jobAddress", "%" + orderBook.getJobAddress().toLowerCase() + "%");
			query2.setParameter("jobAddress", "%" + orderBook.getJobAddress().toLowerCase() + "%");
		}
		if (orderBook.getAttn() != null && !orderBook.getAttn().isEmpty()) {
			query.setParameter("attn", orderBook.getAttn().toLowerCase());
			query2.setParameter("attn", orderBook.getAttn().toLowerCase());
		}
		if (orderBook.getOrbf() != null && !orderBook.getOrbf().isEmpty()) {
			query.setParameter("orbf", orderBook.getOrbf().toLowerCase());
			query2.setParameter("orbf", orderBook.getOrbf().toLowerCase());
		}
		if (orderBook.getJobOrderNumber() != null && !orderBook.getJobOrderNumber().isEmpty()) {
			query.setParameter("jobOrderNumber", orderBook.getJobOrderNumber());
			query2.setParameter("jobOrderNumber", orderBook.getJobOrderNumber());
		}
		if (orderBook.getComment() != null && !orderBook.getComment().isEmpty()) {
			query.setParameter("comment", "%" + orderBook.getComment().toLowerCase() + "%");
			query2.setParameter("comment", "%" + orderBook.getComment().toLowerCase() + "%");
		}
		if (orderBook.getEstDate() != null) {
			query.setParameter("estDate", orderBook.getEstDate().getTime(), TemporalType.DATE);
			query2.setParameter("estDate", orderBook.getEstDate().getTime(), TemporalType.DATE);
		}
		/**
		 * Search fields
		 */

		if (orderBook.getPageNumber() > 0 && orderBook.getPageSize() > 0) {
			query.setFirstResult(((orderBook.getPageNumber() - 1) * orderBook.getPageSize()))
					.setMaxResults(orderBook.getPageSize());
		}
		StsResponse<OrderBook> response = new StsResponse<OrderBook>();
		if (orderBook.getPageNumber() <= 1) {
			Object object = query2.getSingleResult();
			int count = 0;
			if (object != null) {
				count = (((Long) object).intValue());
			}
			response.setTotalCount(count);
		}

		@SuppressWarnings("unchecked")
		List<OrderBook> results = query.getResultList();
		if (results != null && !results.isEmpty()) {
			for (OrderBook result : results) {
				enrichOrderBookForReport(result);
			}
			response.setResults(results);
		}
		return response;
	}

	private OrderBook enrichOrderBookForReport(OrderBook orderBook) {
		if (orderBook.getSalesPersonId() != null && orderBook.getSalesPersonId() > 0) {
			User user = userService.findUser(orderBook.getSalesPersonId());
			if (user != null) {
				orderBook.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
		}

		if (orderBook.getContractorId() != null && orderBook.getContractorId() > 0) {
			orderBook.setContractorName(
					getEntityManager().find(Contractor.class, orderBook.getContractorId()).getName());
		}
		if (orderBook.getShipToId() != null && orderBook.getShipToId() > 0) {
			orderBook.setShipToName(getEntityManager().find(ShipTo.class, orderBook.getShipToId()).getShipToAddress());
		}
		if (orderBook.getFactoryId() != null && orderBook.getFactoryId() > 0) {
			orderBook.setFactoryName(getEntityManager().find(Factory.class, orderBook.getFactoryId()).getFactoryName());
		}

		return orderBook;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Orbf getOrbf(Long id, Long orderBookId) {
		List<Orbf> orbfs = getEntityManager().createNamedQuery("Orbf.findByIdAndByOrderBookId").setParameter("id", id)
				.setParameter("orderBookId", orderBookId).getResultList();
		if (orbfs != null && !orbfs.isEmpty()) {
			return orbfs.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public Orbf saveOrbf(Orbf orbf) {
		if (orbf.getId() != null && orbf.getId() > 0) {
			getEntityManager().merge(orbf);
		} else {
			getEntityManager().persist(orbf);
		}
		OrderBook ob = getEntityManager().find(OrderBook.class, orbf.getOrderBookId());
		ob.setEstDate(orbf.getEstDate());
		ob.setOrbf(orbf.getOrbf());
		getEntityManager().merge(ob);
		getEntityManager().flush();
		return orbf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Orbf getOrbf(Long orderBookId) {
		List<Orbf> orbfs = getEntityManager().createNamedQuery("Orbf.findByOrderBookId")
				.setParameter("orderBookId", orderBookId).getResultList();
		List<ShipmentStatus> sss = getEntityManager().createNamedQuery("ShipmentStatus.findByOrderBook")
				.setParameter("orderBookId", orderBookId).getResultList();
		Orbf orbf = null;
		if (orbfs != null && !orbfs.isEmpty()) {
			orbf = orbfs.get(0);
		}else{
			orbf = new Orbf();
		}
		if (sss != null && !sss.isEmpty() && orbf != null) {
			if (sss.size() == 1) {
				orbf.setStatusOne(sss.get(0).getCurrStatusText());
				orbf.setTruckerOne(sss.get(0).getTruckerId());
				orbf.setTrakingLinkOne(sss.get(0).getTrackingUrl());
			}
			if (sss.size() == 2) {
				orbf.setStatusTwo(sss.get(1).getCurrStatusText());
				orbf.setTruckerTwo(sss.get(1).getTruckerId());
				orbf.setTrackingLinkTwo(sss.get(1).getTrackingUrl());
			}
			if (sss.size() == 3) {
				orbf.setStatusThree(sss.get(2).getCurrStatusText());
				orbf.setTruckerThree(sss.get(2).getTruckerId());
				orbf.setTrackingLinkThree(sss.get(2).getTrackingUrl());
			}
		}
		return orbf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<OrderBook> getOrders(String invType, int pageNumber, int pageSize) {

		List<OrderBook> oBooks = null;
		// Get all order book IDs
		Query query = getEntityManager()
				.createQuery(
						"SELECT DISTINCT(obii.bookId) FROM OrderBookInvItem obii WHERE LOWER(obii.invType) =:invType")
				.setParameter("invType", invType.toLowerCase());

		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}

		List<Long> list = query.getResultList();
		if (list != null && !list.isEmpty()) {
			oBooks = new ArrayList<OrderBook>();
			OrderBook ob = null;
			Query itemQuery = null;
			Double netCost = 0.0D;
			for (Long id : list) {
				netCost = 0.0D;
				ob = getOrderBook(id);
				itemQuery = getEntityManager().createNamedQuery("OrderBookInvItem.getAllByOrderBookId")
						.setParameter("bookId", ob.getId());
				List<OrderBookInvItem> results = itemQuery.getResultList();
				if (results != null && !results.isEmpty()) {
					Set<OrderBookInvItem> items = new HashSet<OrderBookInvItem>();
					for (OrderBookInvItem item : results) {
						enricInvItem(item);
						items.add(item);
						netCost += item.getNetCost();
					}
					if (!items.isEmpty()) {
						ob.setInvItems(items);
					}
				}
				ob.setNetCost(netCost);
				oBooks.add(ob);
			}
		}
		List<Object> objects = getEntityManager()
				.createQuery(
						"SELECT DISTINCT(obii.bookId) FROM OrderBookInvItem obii WHERE LOWER(obii.invType) =:invType")
				.setParameter("invType", invType.toLowerCase()).getResultList();
		int totalCount = 0;
		if (objects != null && !objects.isEmpty()) {
			totalCount = (((Long) objects.get(0)).intValue());
		}
		StsResponse<OrderBook> response = new StsResponse<OrderBook>();
		response.setTotalCount(totalCount);
		if (oBooks != null) {
			response.setResults(oBooks);
		}
		return response;
	}

	private void enricInvItems(Set<OrderBookInvItem> items) {
		for (OrderBookInvItem obii : items) {
			enricInvItem(obii);
		}
	}

	private void enricInvItem(OrderBookInvItem item) {
		if (item.getInvType().equalsIgnoreCase("aw")) { // aw inventory
			AwInventory inv = getEntityManager().find(AwInventory.class, item.getInvItemId());
			item.setBillableCost(inv.getBillableCost());
			item.setListPrice(inv.getUnitPrice());
		} else if (item.getInvType().equalsIgnoreCase("awf")) {
			AwfInventory inv = getEntityManager().find(AwfInventory.class, item.getInvItemId());
			item.setBillableCost(inv.getBillableCost());
			item.setListPrice(inv.getUnitPrice());
		} else if (item.getInvType().equalsIgnoreCase("sbc")) {
			SbcInventory inv = getEntityManager().find(SbcInventory.class, item.getInvItemId());
			item.setBillableCost(inv.getBillableCost());
			item.setListPrice(inv.getUnitPrice());
		} else if (item.getInvType().equalsIgnoreCase("spl")) {
			SplInventory inv = getEntityManager().find(SplInventory.class, item.getInvItemId());
			item.setBillableCost(inv.getBillableCost());
			item.setListPrice(inv.getUnitPrice());
		} else if (item.getInvType().equalsIgnoreCase("j")) {
			JInventory inv = getEntityManager().find(JInventory.class, item.getInvItemId());
			item.setBillableCost(inv.getBillableCost());
			item.setListPrice(inv.getUnitPrice());
		} else if (item.getInvType().equalsIgnoreCase("q")) {
		}
		item.setNetCost((item.getListPrice() * item.getOrderQty()));
	}

	@Override
	public OrderBook fetchPremiumOrder(Long orderBookId) {
		OrderBook ob = getOrderBook(orderBookId);
		JobOrder jo = getEntityManager().find(JobOrder.class, ob.getJobId());
		if (jo.getPoName() != null) {
			ob.setPo(jo.getPoName());
		}
		User user = getEntityManager().find(User.class, ob.getSalesPersonId());
		String userName = user.getFirstName();
		if (user.getLastName() != null) {
			userName = userName + " " + user.getLastName();
		}
		ob.setSalesPersonName(userName);
		// Set shipment status of this order
		ob.setDeliveryStatuses(getShipmentStatus(ob.getId()));
		return ob;
	}

	@Override
	public OrderBook getOrderBook(String orderBookNumber) {
		@SuppressWarnings("unchecked")
		List<OrderBook> obs = getEntityManager().createNamedQuery("OrderBook.getByNumber")
				.setParameter("orderBookNumber", orderBookNumber).getResultList();
		if (obs != null && !obs.isEmpty()) {
			OrderBook ob = obs.get(0);
			ob.setDeliveryStatuses(getShipmentStatus(ob.getId()));
			return ob;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentStatus> getShipmentStatus(Long orderBookId) {
		return getEntityManager().createNamedQuery("ShipmentStatus.findByOrderBook")
				.setParameter("orderBookId", orderBookId).getResultList();
	}

	@Override
	@Transactional
	public ShipmentStatus saveShipmentStatus(ShipmentStatus shipmentStatus) {
		if (shipmentStatus.getId() != null) {
			return getEntityManager().merge(shipmentStatus);
		}
		getEntityManager().persist(shipmentStatus);
		getEntityManager().flush();
		return shipmentStatus;
	}

	@Transactional
	@Override
	public ClaimFollowup saveClaimFollowup(ClaimFollowup cf) {
		if (cf.getId() != null) {
			getEntityManager().merge(cf);
		} else {
			getEntityManager().persist(cf);
		}
		getEntityManager().flush();
		return cf;
	}

	@Override
	public ClaimFollowup getClaimFollowup(Long id) {
		return getEntityManager().find(ClaimFollowup.class, id);
	}

	@Override
	public List<ClaimFollowup> getFollowupsByOrderBook(Long orderBookId) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClaimFollowup> getFollowupsByClaim(Long claimId, String source) {
		return getEntityManager().createNamedQuery("ClaimFollowup.getByClaim").setParameter("claimId", claimId).setParameter("source", source.toLowerCase())
				.getResultList();
	}
}
