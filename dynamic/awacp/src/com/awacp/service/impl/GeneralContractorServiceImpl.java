package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Engineer;
import com.awacp.entity.GeneralContractor;
import com.awacp.service.GeneralContractorService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class GeneralContractorServiceImpl extends CommonServiceImpl<GeneralContractor>
		implements GeneralContractorService {
	@Autowired
	UserService userService;
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<GeneralContractor> listContractors(int pageNumber, int pageSize) {
		return initAdditionalInfo(
				listAll(pageNumber, pageSize, GeneralContractor.class.getSimpleName(), getEntityManager()));

	}

	private StsResponse<GeneralContractor> initAdditionalInfo(StsResponse<GeneralContractor> results) {
		if (results.getResults() == null)
			return null;
		for (GeneralContractor object : results.getResults()) {
			User user = userService.findUser(object.getSalesPerson());
			if (user != null) {
				object.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
		}
		return results;
	}

	@Override
	public GeneralContractor getContractor(Long id) {
		return getEntityManager().find(GeneralContractor.class, id);
	}

	@Override
	@Transactional
	public GeneralContractor saveContractor(GeneralContractor contractor) throws StsDuplicateException {
		if (isExistsByEmail(contractor.getEmail(), "GeneralContractor", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	@Transactional
	public GeneralContractor updateContractor(GeneralContractor contractor) throws StsDuplicateException {
		GeneralContractor object = getByEmail(contractor.getEmail(), "GeneralContractor", getEntityManager());
		if (object != null && object.getId() != contractor.getId()) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		GeneralContractor entity = getContractor(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
