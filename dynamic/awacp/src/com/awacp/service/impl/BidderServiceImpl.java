package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.service.BidderService;

public class BidderServiceImpl implements BidderService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bidder> listBidders(int pageNumber, int pageSize) {
		int fResult = ((pageNumber - 1) * pageSize);
		return getEntityManager().createNamedQuery("Bidder.listAll").setFirstResult(fResult).setMaxResults(pageSize)
				.getResultList();
	}

	@Override
	public Bidder getBidder(Long bidderId) {
		return getEntityManager().find(Bidder.class, bidderId);
	}

	@Override
	@Transactional
	public Bidder saveBidder(Bidder bidder) {
		getEntityManager().persist(bidder);
		getEntityManager().flush();
		return bidder;
	}

	@Override
	@Transactional
	public Bidder updateBidder(Bidder bidder) {
		return getEntityManager().merge(bidder);
	}
}
