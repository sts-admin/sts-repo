package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;
import com.awacp.entity.Architect;
import com.awacp.service.ArchitectService;
import com.sts.core.dto.StsResponse;

public class ArchitectServiceImpl implements ArchitectService {
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
	public StsResponse<com.awacp.entity.Architect> listArchitects(int pageNumber, int pageSize) {
		StsResponse<com.awacp.entity.Architect> response = new StsResponse<com.awacp.entity.Architect>();
		if (pageNumber <= 1) {
			Object object = getEntityManager().createNamedQuery("Architect.countAll").getSingleResult();
			if (object != null) {
				response.setTotalCount(((Long) object).intValue());
			}
		}
		Query query = getEntityManager().createNamedQuery("Architect.listAll");
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Architect> architects = query.getResultList();
		return architects == null || architects.isEmpty() ? response : response.setResults(architects);

	}


	@Override
	public Architect getArchitect(Long id) {
		return getEntityManager().find(Architect.class, id);
	}

	@Override
	@Transactional
	public Architect saveArchitect(Architect architect) {
		getEntityManager().persist(architect);
		getEntityManager().flush();
		return architect;
	}

	@Override
	@Transactional
	public Architect updateArchitect(Architect architect) {
		architect = getEntityManager().merge(architect);
		getEntityManager().flush();
		return architect;
	}

}
