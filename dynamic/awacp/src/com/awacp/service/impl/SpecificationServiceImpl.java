package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Specification;
import com.awacp.service.SpecificationService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class SpecificationServiceImpl extends CommonServiceImpl<Specification>implements SpecificationService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Specification> listSpecifications(int pageNumber, int pageSize) {
		return listAll(pageNumber, pageSize, Specification.class.getSimpleName(), getEntityManager());
	}

	@Override
	public Specification getSpecification(Long id) {
		return getEntityManager().find(Specification.class, id);
	}

	@Override
	@Transactional
	public Specification saveSpecification(Specification specification) {
		getEntityManager().persist(specification);
		getEntityManager().flush();
		return specification;
	}

	@Override
	@Transactional
	public Specification updateSpecification(Specification specification){
		specification = getEntityManager().merge(specification);
		getEntityManager().flush();
		return specification;
	}

}
