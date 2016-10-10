package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.service.BidderService;
import com.sts.core.dto.StsResponse;

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
	public Bidder saveBidder(Bidder bidder) {
		if(bidder.getId() == null){
			getEntityManager().persist(bidder);
			getEntityManager().flush();
		}else{
			getEntityManager().merge(bidder);
			getEntityManager().flush();
		}
		return bidder;
	}

	@Override
	@Transactional
	public Bidder updateBidder(Bidder bidder) {
		return getEntityManager().merge(bidder);
	}
}
