package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.TaxEntry;
import com.awacp.service.TaxService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class TaxServiceImpl extends CommonServiceImpl<TaxEntry>implements TaxService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<TaxEntry> listTaxEntries(int pageNumber, int pageSize) {
		StsResponse<TaxEntry> results = listAll(pageNumber, pageSize, TaxEntry.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public TaxEntry getTaxEntry(Long id) {
		return getEntityManager().find(TaxEntry.class, id);
	}

	@Override
	@Transactional
	public TaxEntry saveTaxEntry(TaxEntry taxEntry) {
		getEntityManager().persist(taxEntry);
		getEntityManager().flush();
		return taxEntry;
	}

	@Override
	@Transactional
	public TaxEntry updateTaxEntry(TaxEntry taxEntry) {
		getEntityManager().merge(taxEntry);
		getEntityManager().flush();
		return taxEntry;
	}

	

	@Override
	@Transactional
	public String delete(Long id) {
		TaxEntry entity = getTaxEntry(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
