package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.MnDType;
import com.awacp.service.MnDTypeService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class MnDTypeServiceImpl extends CommonServiceImpl<MnDType>implements MnDTypeService {
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
	public StsResponse<MnDType> listMnDTypes(Long mndId, int pageNumber, int pageSize) {
		StsResponse<MnDType> response = new StsResponse<MnDType>();
		if (pageNumber <= 1) {
			response.setTotalCount(getTotalRecords(MnDType.class.getSimpleName(), "id", getEntityManager()));
		}
		Query query = getEntityManager().createNamedQuery("MnD.findAllByMndId").setParameter("mndId", mndId);
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<MnDType> results = query.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(results);
	}

	@Override
	public MnDType getMnDType(Long id) {
		return getEntityManager().find(MnDType.class, id);
	}

	@Override
	@Transactional
	public MnDType saveMnDType(MnDType MnDType) {
		getEntityManager().persist(MnDType);
		getEntityManager().flush();
		return MnDType;
	}

	@Override
	@Transactional
	public MnDType updateMnDType(MnDType MnDType) {
		MnDType = getEntityManager().merge(MnDType);
		getEntityManager().flush();
		return MnDType;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		MnDType entity = getMnDType(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Override
	@Transactional
	public String setMessage(String message, Long id) {
		MnDType type = getMnDType(id);
		if (type != null) {
			type.setMessage(message);
			getEntityManager().persist(type);
			return "success";
		}
		return "fail";
	}

	@Override
	@Transactional
	public String setFlag(boolean flagValue, Long id) {
		MnDType type = getMnDType(id);
		if (type != null) {
			type.setFlag(flagValue);
			getEntityManager().persist(type);
			return "success";
		}
		return "fail";
	}

}
