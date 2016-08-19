/**
 * 
 */
package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.sts.core.entity.ErrorLog;
import com.sts.core.service.ErrorLogService;

public class ErrorLogServiceImpl implements ErrorLogService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public ErrorLog save(ErrorLog log) {
		getEntityManager().persist(log);
		return log;
	}

	@Override
	@Transactional
	public void delete(Long errorLogId) {
		ErrorLog log = getById(errorLogId);
		if (log != null) {
			getEntityManager().remove(log);
		}
	}

	@Override
	@Transactional
	public Integer deleteAll() {
		return getEntityManager().createQuery("DELETE FROM ErrorLog").executeUpdate();
	}

	@Override
	public ErrorLog getById(Long errorLogId) {
		return getEntityManager().find(ErrorLog.class, errorLogId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ErrorLog> getAll() {
		return getEntityManager().createNamedQuery("ErrorLog.findAll").getResultList();
	}
}
