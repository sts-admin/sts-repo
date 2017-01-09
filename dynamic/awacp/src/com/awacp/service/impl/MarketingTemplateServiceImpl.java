package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.MarketingTemplate;
import com.awacp.service.MarketingTemplateService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class MarketingTemplateServiceImpl extends CommonServiceImpl<MarketingTemplate>
		implements MarketingTemplateService {
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
	public StsResponse<MarketingTemplate> listMarketingTemplates(int pageNumber, int pageSize) {
		return initAdditionalInfo(
				listAll(pageNumber, pageSize, MarketingTemplate.class.getSimpleName(), getEntityManager()));
	}

	private StsResponse<MarketingTemplate> initAdditionalInfo(StsResponse<MarketingTemplate> results) {
		if (results.getResults() == null)
			return null;
		return results;
	}

	@Override
	public MarketingTemplate getMarketingTemplate(Long id) {
		return getEntityManager().find(MarketingTemplate.class, id);
	}

	@Override
	@Transactional
	public MarketingTemplate saveMarketingTemplate(MarketingTemplate marketingTemplate) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(marketingTemplate.getTemplateName())
				&& isTemplateExists(marketingTemplate.getTemplateName())) {
			throw new StsDuplicateException("duplicate_template");
		}
		getEntityManager().persist(marketingTemplate);
		getEntityManager().flush();
		return marketingTemplate;
	}

	private boolean isTemplateExists(String templateName) {
		return getByName(templateName) != null;
	}

	@SuppressWarnings("unchecked")
	private MarketingTemplate getByName(String templateName) {
		List<MarketingTemplate> templates = getEntityManager().createNamedQuery("MarketingTemplate.getByName")
				.setParameter("templateName", templateName).getResultList();
		return templates == null || templates.isEmpty() ? null : templates.get(0);
	}

	@Override
	@Transactional
	public MarketingTemplate updateMarketingTemplate(MarketingTemplate marketingTemplate) throws StsDuplicateException {
		if (StringUtils.isNotEmpty(marketingTemplate.getTemplateName())) {
			MarketingTemplate object = getByName(marketingTemplate.getTemplateName());
			if (object != null && object.getId() != marketingTemplate.getId()) {
				throw new StsDuplicateException("duplicate_template");
			}
		}
		marketingTemplate = getEntityManager().merge(marketingTemplate);
		getEntityManager().flush();
		return marketingTemplate;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		MarketingTemplate entity = getMarketingTemplate(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
