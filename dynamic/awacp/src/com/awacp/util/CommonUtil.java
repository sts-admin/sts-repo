package com.awacp.util;

import java.util.List;

import javax.persistence.EntityManager;

public class CommonUtil {

	private EntityManager entityManager;

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unused")
	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public int getTotalCount(String queryString){
		return 0;
	}

	@SuppressWarnings("unchecked")
	public <T extends CommonUtil> T pagination(int pageNumber, int pageSize, String queryString) {

		int fResult = ((pageNumber - 1) * pageSize);
		if (fResult > -1) {
			List<T> results = getEntityManager().createNamedQuery(queryString).setFirstResult(1).setMaxResults(pageSize)
					.getResultList();
			return (T) results;
		} else {
			return null;
		}
	}

}
