package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Engineer;
import com.awacp.entity.Spec;
import com.awacp.service.SpecService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class SpecServiceImpl extends CommonServiceImpl<Spec>implements SpecService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Spec> listSpecs(int pageNumber, int pageSize) {
		StsResponse<Spec> results = listAll(pageNumber, pageSize, Spec.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public Spec getSpec(Long id) {
		return getEntityManager().find(Spec.class, id);
	}

	@Override
	@Transactional
	public Spec saveSpec(Spec spec) {
		getEntityManager().persist(spec);
		getEntityManager().flush();
		return spec;
	}

	@Override
	@Transactional
	public Spec updateSpec(Spec spec) {
		spec = getEntityManager().merge(spec);
		getEntityManager().flush();
		return spec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Spec> filter(String keyword) {
		if (keyword == null || keyword.isEmpty())
			return null;
		return getEntityManager().createNamedQuery("Spec.filterByDetailMatch")
				.setParameter("keyword", "%" + keyword.toLowerCase() + "%").getResultList();
	}

}
