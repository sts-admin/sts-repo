/**
 * 
 */
package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.entity.Factory;
import com.awacp.entity.OrderBook;
import com.awacp.entity.ShipTo;
import com.awacp.service.OrderBookService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class OrderBookServiceImpl extends CommonServiceImpl<OrderBook>implements OrderBookService {

	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public OrderBook save(OrderBook orderBook) {
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
		if (orderBook.getId() != null) {
			getEntityManager().merge(orderBook);
		} else {
			getEntityManager().persist(orderBook);
		}
		getEntityManager().flush();
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
			book.setContractorName(getEntityManager().find(Contractor.class, book.getContractorId()).getName());
			book.setShipToName(getEntityManager().find(ShipTo.class, book.getShipToId()).getShipToAddress());
			book.setFactoryName(getEntityManager().find(Factory.class, book.getFactoryId()).getFactoryName());
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
}
