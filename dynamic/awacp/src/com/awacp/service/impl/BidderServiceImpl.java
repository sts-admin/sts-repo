package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Bidder;
import com.awacp.service.BidderService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class BidderServiceImpl extends CommonServiceImpl<Bidder>implements BidderService {
	@Autowired
	UserService userService;

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Bidder> listBidders(int pageNumber, int pageSize) {
		return initAdditionalInfo(listAll(pageNumber, pageSize, Bidder.class.getSimpleName(), getEntityManager()));

	}
	private StsResponse<Bidder> initAdditionalInfo(StsResponse<Bidder> results) {
		if (results.getResults() == null)
			return null;
		for (Bidder object : results.getResults()) {
			User user = userService.findUser(object.getSalesPerson());
			if (user != null) {
				object.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
		}
		return results;
	}

	@Override
	public Bidder getBidder(Long bidderId) {
		return getEntityManager().find(Bidder.class, bidderId);
	}

	@Override
	@Transactional
	public Bidder saveBidder(Bidder bidder) throws StsDuplicateException {
		if (isExistsByEmail(bidder.getEmail(), "Bidder", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().persist(bidder);
		getEntityManager().flush();
		return bidder;
	}

	@Override
	@Transactional
	public Bidder updateBidder(Bidder bidder) throws StsDuplicateException {
		Bidder object = getByEmail(bidder.getEmail(), "Bidder", getEntityManager());
		if (object != null && object.getId() != bidder.getId()) {
			throw new StsDuplicateException("duplicate_email");
		}
		return getEntityManager().merge(bidder);
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Bidder entity = getBidder(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
