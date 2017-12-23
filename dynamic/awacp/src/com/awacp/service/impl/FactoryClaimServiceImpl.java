package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.entity.FactoryClaim;
import com.awacp.entity.Trucker;
import com.awacp.entity.TruckerClaim;
import com.awacp.service.FactoryClaimService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class FactoryClaimServiceImpl extends CommonServiceImpl<FactoryClaim> implements FactoryClaimService {
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
	public FactoryClaim updateFactoryClaim(FactoryClaim claim) throws StsDuplicateException {
		if (claim.getContractorId() != null) {
			claim.setContractor(getEntityManager().find(Contractor.class, claim.getContractorId()));
		}
		if (claim.getTruckerId() != null) {
			claim.setTrucker(getEntityManager().find(Trucker.class, claim.getTruckerId()));
		}
		getEntityManager().merge(claim);
		getEntityManager().flush();
		return claim;
	}

	@Override
	@Transactional
	public FactoryClaim saveFactoryClaim(FactoryClaim claim) throws StsDuplicateException {
		if (claim.getId() != null) {
			return updateFactoryClaim(claim);
		}
		if (claim.getContractorId() != null) {
			claim.setContractor(getEntityManager().find(Contractor.class, claim.getContractorId()));
		}
		if (claim.getTruckerId() != null) {
			claim.setTrucker(getEntityManager().find(Trucker.class, claim.getTruckerId()));
		}
		claim.setStatus("OPEN");
		getEntityManager().persist(claim);
		getEntityManager().flush();
		return claim;
	}

	@Override
	public FactoryClaim getFactoryClaim(Long id) {
		return getEntityManager().find(FactoryClaim.class, id);
	}

	@Override
	public StsResponse<FactoryClaim> listFactoryClaims(int pageNumber, int pageSize) {
		StsResponse<FactoryClaim> results = listAll(pageNumber, pageSize, FactoryClaim.class.getSimpleName(),
				getEntityManager());
		if (results.getResults() != null && !results.getResults().isEmpty()) {
			for (FactoryClaim tc : results.getResults()) {
				initClaims(tc);
			}
		}

		return results;
	}

	private void initClaims(FactoryClaim claim) {
		if(claim.getSalesmanId() != null){
			claim.setSalesPersonName(getEntityManager().find(User.class, claim.getSalesmanId()).getUserCode());
		}
		
	}

	@Override
	@Transactional
	public String delete(Long id) {
		FactoryClaim entity = getFactoryClaim(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
