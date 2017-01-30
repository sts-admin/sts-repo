package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.InvMultiplier;
import com.awacp.service.InvMultiplierService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.service.impl.CommonServiceImpl;

public class InvMultiplierServiceImpl extends CommonServiceImpl<InvMultiplier>implements InvMultiplierService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<InvMultiplier> listInvMultipliers(int pageNumber, int pageSize) {
		StsResponse<InvMultiplier> results = listAll(pageNumber, pageSize, InvMultiplier.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public InvMultiplier getInvMultiplier(Long id) {
		return getEntityManager().find(InvMultiplier.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public InvMultiplier saveInvMultiplier(InvMultiplier invMultiplier) throws StsCoreException {
		List<InvMultiplier> mps = getEntityManager().createNamedQuery("InvMultiplier.findByInvName")
				.setParameter("invName", invMultiplier.getInventoryName().toLowerCase()).getResultList();
		if (mps != null && mps.size() > 0) {
			throw new StsCoreException(StsCoreConstant.DUPLICATE_MULTIPLIER);
		}
		getEntityManager().persist(invMultiplier);
		getEntityManager().flush();
		return invMultiplier;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public InvMultiplier updateInvMultiplier(InvMultiplier invMultiplier) throws StsCoreException {
		List<InvMultiplier> mps = getEntityManager().createNamedQuery("InvMultiplier.findByInvName")
				.setParameter("invName", invMultiplier.getInventoryName().toLowerCase()).getResultList();
		if (mps != null && mps.size() > 0) {
			if(mps.get(0).getId() != invMultiplier.getId()){
				throw new StsCoreException(StsCoreConstant.DUPLICATE_MULTIPLIER);
			}			
		}
		invMultiplier = getEntityManager().merge(invMultiplier);
		getEntityManager().flush();
		return invMultiplier;
	}

	@Override
	public List<InvMultiplier> filter(String keyword) {
		return null;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		InvMultiplier entity = getInvMultiplier(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
