/**
 * 
 */
package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwInventory;
import com.awacp.entity.AwfInventory;
import com.awacp.entity.Contractor;
import com.awacp.entity.Factory;
import com.awacp.entity.JInventory;
import com.awacp.entity.OrderBook;
import com.awacp.entity.OrderBookInvItem;
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
		if (orderBook.getInvItems() != null && orderBook.getInvItems().isEmpty()) {
			orderBook.getInvItems().clear();
		}
		if (orderBook.getId() != null) {
			getEntityManager().merge(orderBook);
			getEntityManager().flush();
		} else {
			DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
														// digits
			String JOBN = new StringBuffer("JOBN").append("-").append(df.format(Calendar.getInstance().getTime()))
					.append("-").append(orderBook.getObCategory()).append("-").toString();
			orderBook.setOrderBookNumber(JOBN);
			getEntityManager().persist(orderBook);
			getEntityManager().flush();
			StringBuffer sbJOBN = new StringBuffer(orderBook.getOrderBookNumber()).append(orderBook.getId());
			if (orderBook.getSpecialInstruction() != null && !orderBook.getSpecialInstruction().isEmpty()) {
				sbJOBN.append("-").append(orderBook.getSpecialInstruction());
			}
			orderBook.setOrderBookNumber(sbJOBN.toString());
			getEntityManager().merge(orderBook);
			getEntityManager().flush();
		}
		// Update inv line item qty to reflect updated stock.
		if (orderBook.getInvItems() != null && !orderBook.getInvItems().isEmpty()) {
			for (OrderBookInvItem obItem : orderBook.getInvItems()) {
				debitInvItemQty(obItem.getInvItemId(), orderBook.getObCategory(), obItem.getOrderQty());
			}
		}
		
		invoiceService.addDummyInvoice(orderBook.getId());
		return orderBook;
	}

	@Override
	public OrderBook getOrderBook(Long orderBookId) {
		return getEntityManager().find(OrderBook.class, orderBookId);
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

	@Override
	@Transactional
	public int debitInvItemQty(Long lineItemId, String invName, int orderQty) {
		invName = invName.trim();
		if (invName.equalsIgnoreCase("j")) {
			JInventory jInv = getEntityManager().find(JInventory.class, lineItemId);
			jInv.setQuantity(jInv.getQuantity() - orderQty);
			getEntityManager().merge(jInv);
		} else if (invName.equalsIgnoreCase("aw")) {
			AwInventory awInv = getEntityManager().find(AwInventory.class, lineItemId);
			awInv.setQuantity(awInv.getQuantity() - orderQty);
			getEntityManager().merge(awInv);
		} else if (invName.equalsIgnoreCase("awf")) {
			AwfInventory awf = getEntityManager().find(AwfInventory.class, lineItemId);
			awf.setQuantity(awf.getQuantity() - orderQty);
			getEntityManager().merge(awf);
		} else if (invName.equalsIgnoreCase("sbc")) {
			SbcInventory sbcInv = getEntityManager().find(SbcInventory.class, lineItemId);
			sbcInv.setQuantity(sbcInv.getQuantity() - orderQty);
			getEntityManager().merge(sbcInv);
		} else if (invName.equalsIgnoreCase("spl")) {
			SplInventory splInv = getEntityManager().find(SplInventory.class, lineItemId);
			splInv.setQuantity(splInv.getQuantity() - orderQty);
			getEntityManager().merge(splInv);
		}
		getEntityManager().flush();
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderBook> listByJobOrder(Long jobOrderId) {
		return getEntityManager().createNamedQuery("OrderBook.getByJobOrderId").setParameter("jobId", jobOrderId).getResultList();
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
}
