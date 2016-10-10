package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.service.BidderService;
import com.sts.core.dto.StsResponse;

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
	public StsResponse<com.awacp.entity.Bidder> listBidders(int pageNumber, int pageSize) {
		StsResponse<com.awacp.entity.Bidder> response = new StsResponse<com.awacp.entity.Bidder>();
		if (pageNumber <= 1) {
			Object object = getEntityManager().createNamedQuery("Bidder.countAll").getSingleResult();
			if (object != null) {
				response.setTotalCount(((Long) object).intValue());
			}
		}
		Query query = getEntityManager().createNamedQuery("Bidder.listAll");
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Bidder> bidders = query.getResultList();
		return bidders == null || bidders.isEmpty() ? response : response.setResults(bidders);

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
