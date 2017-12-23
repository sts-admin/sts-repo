package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.entity.Trucker;
import com.awacp.entity.TruckerClaim;
import com.awacp.service.TruckerClaimService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class TruckerClaimServiceImpl extends CommonServiceImpl<TruckerClaim> implements TruckerClaimService {
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
	public TruckerClaim updateTruckerClaim(TruckerClaim claim) throws StsDuplicateException {
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
	public TruckerClaim saveTruckerClaim(TruckerClaim claim) throws StsDuplicateException {
		if (claim.getId() != null) {
			return updateTruckerClaim(claim);
		}
		if (claim.getContractorId() != null) {
			claim.setContractor(getEntityManager().find(Contractor.class, claim.getContractorId()));
		}
		if (claim.getTruckerId() != null) {
			claim.setTrucker(getEntityManager().find(Trucker.class, claim.getTruckerId()));
		}
		claim.setStatus("CLOSED");
		getEntityManager().persist(claim);
		getEntityManager().flush();
		return claim;
	}

	@Override
	public TruckerClaim getTruckerClaim(Long id) {
		return getEntityManager().find(TruckerClaim.class, id);
	}

	@Override
	public StsResponse<TruckerClaim> listTruckerClaims(int pageNumber, int pageSize) {
		StsResponse<TruckerClaim> results = listAll(pageNumber, pageSize, TruckerClaim.class.getSimpleName(),
				getEntityManager());
		if (results.getResults() != null && !results.getResults().isEmpty()) {
			for (TruckerClaim tc : results.getResults()) {
				initClaims(tc);
			}
		}

		return results;
	}

	private void initClaims(TruckerClaim claim) {
		if(claim.getSalesmanId() != null)
		claim.setSalesPersonName(getEntityManager().find(User.class, claim.getSalesmanId()).getUserCode());
	}

	@Override
	@Transactional
	public String delete(Long id) {
		TruckerClaim entity = getTruckerClaim(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
