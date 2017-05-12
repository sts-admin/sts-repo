package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.PageSetting;
import com.awacp.service.PageSettingService;
import com.sts.core.service.impl.CommonServiceImpl;

public class PageSettingServiceImpl extends CommonServiceImpl<PageSetting>implements PageSettingService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public PageSetting getSiteSetting(Long id) {
		return getEntityManager().find(PageSetting.class, id);
	}

	@Override
	@Transactional
	public PageSetting saveSiteSetting(PageSetting PageSetting) {
		getEntityManager().persist(PageSetting);
		getEntityManager().flush();
		return PageSetting;
	}

	@Override
	@Transactional
	public PageSetting updateSiteSetting(PageSetting PageSetting) {
		PageSetting = getEntityManager().merge(PageSetting);
		getEntityManager().flush();
		return PageSetting;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		PageSetting entity = getSiteSetting(id);
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
	public PageSetting getSiteSetting(String viewName) {
		List<PageSetting> settings = getEntityManager().createNamedQuery("PageSetting.getPageSizeByViewName")
				.setParameter("viewName", viewName).getResultList();
		return settings == null || settings.isEmpty() ? null : settings.get(0);
	}

	@Override
	@Transactional
	public String setPageSizeByView(String viewName, Integer size) {
		PageSetting setting = getSiteSetting(viewName);
		if (setting != null) {
			setting.setPageSize(size);
			getEntityManager().merge(setting);
			return "success";
		} else {
			PageSetting ss = new PageSetting();
			ss.setPageSize(size);
			ss.setViewName(viewName);
			getEntityManager().persist(ss);
			return "success";
		}
	}

}
