package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sts.core.dto.StsResponse;
import com.sts.core.service.CommonService;

public class CommonServiceImpl<T> implements CommonService<T> {

	@Override
	public int getTotalRecords(String entityClassName, String primaryKeyName, EntityManager em) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(entity.").append(primaryKeyName).append(") FROM ")
				.append(entityClassName).append(" entity WHERE entity.archived = 'false'");
		return getTotalRecords(sb.toString(), em);
	}

	@Override
	public int getTotalRecords(String queryString, EntityManager em) {
		Object object = em.createQuery(queryString).getSingleResult();
		if (object != null) {
			return (((Long) object).intValue());
		}
		return 0;
	}

	@Override
	public StsResponse<T> listAll(String entityClassName, EntityManager em) {
		return listAll(-1, -1, entityClassName, em);
	}

	@Override
	public StsResponse<T> listAll(int pageNumber, int pageSize, String entityClassName, EntityManager em) {
		StringBuffer sb = new StringBuffer("SELECT entity FROM ").append(entityClassName)
				.append(" entity WHERE entity.archived = 'false'");
		return listAll(pageNumber, pageSize, sb.toString(), entityClassName, "id", em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<T> listAll(int pageNumber, int pageSize, String queryString, String entityClassName,
			String primaryKeyName, EntityManager em) {
	
		StsResponse<T> response = new StsResponse<T>();
		if (pageNumber <= 1) {
			response.setTotalCount(getTotalRecords(entityClassName, primaryKeyName, em));
		}
		Query query = em.createQuery(queryString);
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<T> results = query.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(results);
	}

	@Override
	public boolean isExistsByEmail(String email, String entityName, EntityManager em) {
		StringBuffer query = new StringBuffer("SELECT COUNT(entity.id) FROM ").append(entityName)
				.append(" entity WHERE entity.archived = 'false' AND  LOWER(entity.email) = :email");
		int count = ((Number) em.createQuery(query.toString()).setParameter("email", email).getSingleResult())
				.intValue();
		return count <= 0 ? false : true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getByEmail(String email, String entityName, EntityManager em) {
		StringBuffer query = new StringBuffer("SELECT entity FROM ").append(entityName)
				.append(" entity WHERE entity.archived = 'false' AND  LOWER(entity.email) = :email");
		List<T> results = em.createQuery(query.toString()).setParameter("email", email).getResultList();
		return results == null || results.isEmpty() ? null : results.get(0);
	}

}
