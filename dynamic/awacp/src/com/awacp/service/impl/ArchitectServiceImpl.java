package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.service.ArchitectService;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class ArchitectServiceImpl extends CommonServiceImpl<Architect> implements ArchitectService {
	private EntityManager entityManager;

	@Autowired
	UserService userService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Architect> listArchitects(int pageNumber, int pageSize) {
		return initAdditionalInfo(listAll(pageNumber, pageSize, Architect.class.getSimpleName(), getEntityManager()));
	}

	private StsResponse<Architect> initAdditionalInfo(StsResponse<Architect> results) {
		if (results.getResults() == null)
			return null;
		for (Architect object : results.getResults()) {
			User user = userService.findUser(object.getSalesPerson());
			if (user != null) {
				object.setSalesPersonName(user.getFirstName() + " " + user.getLastName());
			}
		}
		return results;
	}

	@Override
	public Architect getArchitect(Long id) {
		return getEntityManager().find(Architect.class, id);
	}

	@Override
	@Transactional
	public Architect saveArchitect(Architect architect) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(architect.getEmail())
				&& isExistsByEmail(architect.getEmail(), "Architect", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}

		if (isExistsByName(architect.getName(), "name", architect.getClass().getSimpleName(), getEntityManager())) {
			throw new StsDuplicateException("duplicate_name");
		}
		getEntityManager().persist(architect);
		getEntityManager().flush();
		return architect;
	}

	@Override
	@Transactional
	public Architect updateArchitect(Architect architect) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(architect.getEmail())) {
			Architect object = getByEmail(architect.getEmail(), "Architect", getEntityManager());
			if (object != null && object.getId() != architect.getId()) {
				throw new StsDuplicateException("duplicate_email");
			}
		}

		architect = getEntityManager().merge(architect);
		getEntityManager().flush();
		return architect;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Architect> filter(String keyword) {
		if (keyword == null || keyword.isEmpty())
			return null;
		return getEntityManager().createNamedQuery("Architect.filterByNameMatch")
				.setParameter("keyword", "%" + keyword.toLowerCase() + "%").getResultList();
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Architect entity = getArchitect(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
