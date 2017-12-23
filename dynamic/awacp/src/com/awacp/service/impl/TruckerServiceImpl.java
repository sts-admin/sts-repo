package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Trucker;
import com.awacp.service.TruckerService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class TruckerServiceImpl extends CommonServiceImpl<Trucker> implements TruckerService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Trucker> listTruckers(int pageNumber, int pageSize) {
		StsResponse<Trucker> results = listAll(pageNumber, pageSize, Trucker.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public Trucker getTrucker(Long id) {
		return getEntityManager().find(Trucker.class, id);
	}

	@Override
	@Transactional
	public Trucker saveTrucker(Trucker trucker) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(trucker.getEmail())
				&& isExistsByEmail(trucker.getEmail(), "Trucker", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}

		if (isExistsByName(trucker.getName(), "name", trucker.getClass().getSimpleName(), getEntityManager())) {
			throw new StsDuplicateException("duplicate_name");
		}

		getEntityManager().persist(trucker);
		getEntityManager().flush();
		return trucker;
	}
	
	
	
	
	
	

	@Override
	@Transactional
	public Trucker updateTrucker(Trucker trucker) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(trucker.getEmail())) {
			Trucker object = getByEmail(trucker.getEmail(), "Trucker", getEntityManager());
			if (object != null && object.getId() != trucker.getId()) {
				throw new StsDuplicateException("duplicate_email");
			}
		}
		getEntityManager().merge(trucker);
		getEntityManager().flush();
		return trucker;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Trucker entity = getTrucker(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
