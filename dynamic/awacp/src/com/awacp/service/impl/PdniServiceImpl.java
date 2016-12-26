package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.MnD;
import com.awacp.entity.Pdni;
import com.awacp.service.PdniService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class PdniServiceImpl extends CommonServiceImpl<Pdni>implements PdniService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Pdni> listPdnis(int pageNumber, int pageSize) {
		StsResponse<Pdni> results = listAll(pageNumber, pageSize, Pdni.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public Pdni getPdni(Long id) {
		return getEntityManager().find(Pdni.class, id);
	}

	@Override
	@Transactional
	public Pdni savePdni(Pdni pdni) {
		getEntityManager().persist(pdni);
		getEntityManager().flush();
		return pdni;
	}

	@Override
	@Transactional
	public Pdni updatePdni(Pdni pdni) {
		pdni = getEntityManager().merge(pdni);
		getEntityManager().flush();
		return pdni;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Pdni entity = getPdni(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}


}
