package com.awacp.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.service.BidderService;
import com.awacp.util.PaginationUtil;

public class BidderServiceImpl extends PaginationUtil implements BidderService  {

	private EntityManager entityManager;
    int PAGESIZE = 4;
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bidder> listBidders(int pageNumber,int pageSize) {
		Bidder bidder = new Bidder();
		long count = 0L;
		List<Bidder> listOfBidders = new ArrayList<>();
		String qryName = "Bidder.listAll";
		if(pageNumber == 1){
			count = ((Long)getEntityManager().createNamedQuery(qryName+"Count").getSingleResult()).intValue();
		}
		List <Bidder> bidders = pagination(pageNumber,pageSize,qryName);
		bidders.get(0).setCountBidders(count);
		return bidders;
		
		
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
