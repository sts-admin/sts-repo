package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.entity.Contractor;
import com.awacp.service.BidderService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class BidderServiceImpl extends CommonServiceImpl<Bidder>implements BidderService {

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
		return listAll(pageNumber, pageSize, Bidder.class.getSimpleName(), getEntityManager());

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
}
