package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Spec;
import com.awacp.service.SpecificationService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class SpecificationServiceImpl extends CommonServiceImpl<Spec>implements SpecificationService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Spec> listSpecifications(int pageNumber, int pageSize) {
		System.err.println("SpecificationServiceImpl :: listSpecifications");
		StsResponse<Spec> results = listAll(pageNumber, pageSize, Spec.class.getSimpleName(), getEntityManager());
		if(results != null && results.getResults() != null){
			for(Spec spec: results.getResults()){
				System.err.println("Spec = "+ spec.getDetail());
			}
		}
		return results;
	}

	@Override
	public Spec getSpecification(Long id) {
		return getEntityManager().find(Spec.class, id);
	}

	@Override
	@Transactional
	public Spec saveSpecification(Spec spec) {
		getEntityManager().persist(spec);
		getEntityManager().flush();
		return spec;
	}

	@Override
	@Transactional
	public Spec updateSpecification(Spec spec){
		spec = getEntityManager().merge(spec);
		getEntityManager().flush();
		return spec;
	}

}
