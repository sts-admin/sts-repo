package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.SiteSetting;
import com.awacp.service.SiteSettingService;
import com.sts.core.service.impl.CommonServiceImpl;

public class SiteSettingServiceImpl extends CommonServiceImpl<SiteSetting>implements SiteSettingService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public SiteSetting getSiteSetting(Long id) {
		return getEntityManager().find(SiteSetting.class, id);
	}

	@Override
	@Transactional
	public SiteSetting saveSiteSetting(SiteSetting SiteSetting) {
		getEntityManager().persist(SiteSetting);
		getEntityManager().flush();
		return SiteSetting;
	}

	@Override
	@Transactional
	public SiteSetting updateSiteSetting(SiteSetting SiteSetting) {
		SiteSetting = getEntityManager().merge(SiteSetting);
		getEntityManager().flush();
		return SiteSetting;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		SiteSetting entity = getSiteSetting(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public SiteSetting getSiteSetting(String viewName) {
		List<SiteSetting> settings = getEntityManager().createNamedQuery("SiteSetting.getPageSizeByViewName")
				.setParameter("viewName", viewName).getResultList();
		return settings == null || settings.isEmpty() ? null : settings.get(0);
	}

	@Override
	@Transactional
	public String setPageSizeByView(String viewName, Integer size) {
		SiteSetting setting = getSiteSetting(viewName);
		if (setting != null) {
			setting.setPageSize(size);
			getEntityManager().merge(setting);
			return "success";
		} else {
			SiteSetting ss = new SiteSetting();
			ss.setPageSize(size);
			ss.setViewName(viewName);
			getEntityManager().persist(ss);
			return "success";
		}
	}

}
