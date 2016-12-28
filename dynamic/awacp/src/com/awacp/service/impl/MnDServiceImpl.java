package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.MnD;
import com.awacp.service.MnDService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class MnDServiceImpl extends CommonServiceImpl<MnD>implements MnDService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<MnD> listMnDs(int pageNumber, int pageSize) {
		StsResponse<MnD> results = listAll(pageNumber, pageSize, MnD.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public MnD getMnD(Long id) {
		return getEntityManager().find(MnD.class, id);
	}

	@Override
	@Transactional
	public MnD saveMnD(MnD mnD) {
		getEntityManager().persist(mnD);
		getEntityManager().flush();
		return mnD;
	}

	@Override
	@Transactional
	public MnD updateMnD(MnD mnD) {
		mnD = getEntityManager().merge(mnD);
		getEntityManager().flush();
		return mnD;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		MnD entity = getMnD(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
