package com.awacp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.entity.GeneralContractor;
import com.awacp.entity.Takeoff;
import com.awacp.service.BidderService;
import com.awacp.service.ContractorService;
import com.awacp.service.TakeoffService;
import com.sts.core.service.UserService;

public class TakeoffServiceImpl implements TakeoffService {

	private EntityManager entityManager;

	@Autowired
	private BidderService bidderService;

	@Autowired
	private ContractorService contractorService;
	
	@Autowired
	private UserService userService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Takeoff> listTakeoffs() {
		return getEntityManager().createNamedQuery("Takeoff.listAll").getResultList();
	}

	@Override
	public Takeoff getTakeoff(Long takeoffId) {
		return getEntityManager().find(Takeoff.class, takeoffId);
	}

	@Override
	@Transactional
	public Takeoff saveTakeoff(Takeoff takeoff) {
		String[] biddersIds = takeoff.getBiddersIds();
		if (biddersIds != null && biddersIds.length > 0) {
			Set<Bidder> bidders = new HashSet<Bidder>();
			for (String id : biddersIds) {
				Bidder bidder = bidderService.getBidder(Long.valueOf(id));
				if (bidder != null) {
					bidders.add(bidder);
				}
			}
			takeoff.setBidders(bidders);
		}
		String[] contractorIds = takeoff.getContractorsIds();
		if (contractorIds != null && contractorIds.length > 0) {
			Set<GeneralContractor> contractors = new HashSet<GeneralContractor>();
			for (String id : contractorIds) {
				GeneralContractor contractor = contractorService.getGenearalContractor(Long.valueOf(id));
				if (contractor != null) {
					contractors.add(contractor);
				}
			}
			takeoff.setGeneralContractors(contractors);
		}
		if (takeoff.getUserNameOrEmail() != null && !takeoff.getUserNameOrEmail().isEmpty()) {
			String code = userService.getUserCode(takeoff.getUserNameOrEmail());
			takeoff.setUserCode(code);
		}
		getEntityManager().persist(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

	@Override
	@Transactional
	public Takeoff updateTakeoff(Takeoff takeoff) {
		takeoff = getEntityManager().merge(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

}
