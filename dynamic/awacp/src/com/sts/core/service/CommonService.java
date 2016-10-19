package com.sts.core.service;

import javax.persistence.EntityManager;

import com.sts.core.dto.StsResponse;

public interface CommonService<T> {

	public int getTotalRecords(String entityClassName, String primaryKeyName, EntityManager em);

	public int getTotalRecords(String queryString, EntityManager em);

	StsResponse<T> listAll(String entityClassName, EntityManager em);

	StsResponse<T> listAll(int pageNumber, int pageSize, String entityClassName, EntityManager em);

	StsResponse<T> listAll(int pageNumber, int pageSize, String queryString, String entityClassName,
			String primaryKeyName, EntityManager em);

	public boolean isExistsByEmail(final String email, final String entityName, EntityManager em);

	public T getByEmail(final String email, final String entityName, EntityManager em);
}
