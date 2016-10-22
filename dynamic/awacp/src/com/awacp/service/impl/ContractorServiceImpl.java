package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.service.ContractorService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class ContractorServiceImpl extends CommonServiceImpl<Contractor>implements ContractorService {
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
	public StsResponse<Contractor> listContractors(int pageNumber, int pageSize) {
		return initAdditionalInfo(listAll(pageNumber, pageSize, Contractor.class.getSimpleName(), getEntityManager()));
	}
	private StsResponse<Contractor> initAdditionalInfo(StsResponse<Contractor> results) {
		if (results.getResults() == null)
			return null;
		for (Contractor object : results.getResults()) {
			User user = userService.findUser(object.getSalesPerson());
			if (user != null) {
				object.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
		}
		return results;
	}

	@Override
	public Contractor getContractor(Long id) {
		return getEntityManager().find(Contractor.class, id);
	}

	@Override
	@Transactional
	public Contractor saveContractor(Contractor contractor) throws StsDuplicateException {
		if (isExistsByEmail(contractor.getEmail(), "Contractor", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	@Transactional
	public Contractor updateContractor(Contractor contractor) throws StsDuplicateException {
		Contractor object = getByEmail(contractor.getEmail(), "Contractor", getEntityManager());
		if (object != null && object.getId() != contractor.getId()) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

}
