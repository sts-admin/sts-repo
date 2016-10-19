package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.entity.Engineer;
import com.awacp.service.EngineerService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class EngineerServiceImpl extends CommonServiceImpl<Engineer>implements EngineerService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Engineer> listEngineers(int pageNumber, int pageSize) {
		return listAll(pageNumber, pageSize, Engineer.class.getSimpleName(), getEntityManager());
	}

	@Override
	public Engineer getEngineer(Long id) {
		return getEntityManager().find(Engineer.class, id);
	}

	@Override
	@Transactional
	public Engineer saveEngineer(Engineer engineer) throws StsDuplicateException {
		if (isExistsByEmail(engineer.getEmail(), "Engineer", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().persist(engineer);
		getEntityManager().flush();
		return engineer;
	}

	@Override
	@Transactional
	public Engineer updateEngineer(Engineer engineer) throws StsDuplicateException {
		Engineer object = getByEmail(engineer.getEmail(), "Engineer", getEntityManager());
		if (object != null && object.getId() != engineer.getId()) {
			throw new StsDuplicateException("duplicate_email");
		}
		engineer = getEntityManager().merge(engineer);
		getEntityManager().flush();
		return engineer;
	}
}
