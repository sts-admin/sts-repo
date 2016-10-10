package com.awacp.util;

import java.util.List;

import javax.persistence.EntityManager;

public class PaginationUtil {

	private EntityManager entityManager;

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unused")
	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public <T extends PaginationUtil> T pagination(int pageNumber, int pageSize, String queryName) {

		int fResult = ((pageNumber - 1) * pageSize);
		if (fResult > -1) {
			List<T> results = getEntityManager().createNamedQuery(queryName).setFirstResult(1).setMaxResults(pageSize)
					.getResultList();
			return (T) results;
		} else {
			return null;
		}

	}

}
