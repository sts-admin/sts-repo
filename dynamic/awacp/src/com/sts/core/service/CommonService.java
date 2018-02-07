package com.sts.core.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sts.core.dto.StsResponse;

public interface CommonService<T> {

	public int getTotalRecords(String entityClassName, String primaryKeyName, EntityManager em);

	public int getTotalRecords(String queryString, EntityManager em);

	public int getTotalRecords(Query query);

	public StsResponse<T> listAll(String entityClassName, EntityManager em);

	public StsResponse<T> listAll(int pageNumber, int pageSize, String entityClassName, EntityManager em);

	public StsResponse<T> listAllArchived(int pageNumber, int pageSize, String entityClassName, EntityManager em);

	public StsResponse<T> listAll(int pageNumber, int pageSize, String searchQuery, String countQuery,
			String entityClassName, String primaryKeyName, EntityManager em);

	public StsResponse<T> listAll(int pageNumber, int pageSize, Query searchQuery, Query countQuery,
			String entityClassName, String primaryKeyName, EntityManager em);

	public boolean isExistsByEmail(final String email, final String entityName, EntityManager em);

	public boolean isExistsByName(final String name, String fieldName, final String entityName, EntityManager em);

	public T getByEmail(final String email, final String entityName, EntityManager em);
}
