package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.awacp.entity.SystemLog;
import com.sts.core.dto.Log;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
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
	public int getTotalRecords(Query query) {
		Object object = query.getSingleResult();
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
	public StsResponse<T> listAllArchived(int pageNumber, int pageSize, String entityClassName, EntityManager em) {
		StringBuffer sb = new StringBuffer("SELECT entity FROM ").append(entityClassName)
				.append(" entity WHERE entity.archived = 'true'");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(entity.id").append(" FROM ").append(entityClassName)
				.append(" entity WHERE entity.archived = 'true'");
		return listAll(pageNumber, pageSize, sb.toString(), countQuery.toString(), entityClassName, "id", em);
	}

	@Override
	public StsResponse<T> listAll(int pageNumber, int pageSize, String entityClassName, EntityManager em) {
		StringBuffer sb = new StringBuffer("SELECT entity FROM ").append(entityClassName)
				.append(" entity WHERE entity.archived = 'false' ORDER BY entity.dateCreated DESC");
		if (entityClassName.equalsIgnoreCase(User.class.getSimpleName())) {
			sb = new StringBuffer("SELECT entity FROM ").append(entityClassName).append(
					" entity WHERE entity.archived = 'false' AND  entity.deleted = 'false' ORDER BY entity.dateCreated DESC");
		}
		return listAll(pageNumber, pageSize, sb.toString(), "all", entityClassName, "id", em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<T> listAll(int pageNumber, int pageSize, String searchQuery, String countQuery,
			String entityClassName, String primaryKeyName, EntityManager em) {

		StsResponse<T> response = new StsResponse<T>();
		if (pageNumber <= 1) {
			if ("all".equalsIgnoreCase(countQuery)) {
				response.setTotalCount(getTotalRecords(entityClassName, primaryKeyName, em));
			} else {
				response.setTotalCount(getTotalRecords(countQuery, em));
			}

		}
		Query query = em.createQuery(searchQuery);
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<T> results = query.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(results);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<T> listAll(int pageNumber, int pageSize, Query searchQuery, Query countQuery,
			String entityClassName, String primaryKeyName, EntityManager em) {

		StsResponse<T> response = new StsResponse<T>();
		if (pageNumber <= 1) {
			if (countQuery == null) {
				response.setTotalCount(getTotalRecords(entityClassName, primaryKeyName, em));
			} else {
				response.setTotalCount(getTotalRecords(countQuery));
			}

		}
		if (pageNumber > 0 && pageSize > 0) {
			searchQuery.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<T> results = searchQuery.getResultList();
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

	@Override
	public boolean isExistsByName(String name, String fieldName, String entityName, EntityManager em) {
		StringBuffer query = new StringBuffer("SELECT COUNT(entity.id) FROM ").append(entityName).append(
				" entity WHERE entity.archived = 'false' AND  LOWER(entity." + fieldName + ") = :" + fieldName + "");
		int count = ((Number) em.createQuery(query.toString()).setParameter(fieldName, name.trim().toLowerCase())
				.getSingleResult()).intValue();
		return count <= 0 ? false : true;
	}

	@Override
	public void doLogActivity(Log log, EntityManager em) {
		SystemLog systemLog = new SystemLog(log.getSection(), log.getDescription(), log.getDateCreated(),
				log.getCreatedBy(), log.getUserCode(), log.getVersion());
		em.persist(systemLog);

	}
}
