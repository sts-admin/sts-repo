package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AppSetting;
import com.awacp.service.AppSettingService;
import com.sts.core.service.impl.CommonServiceImpl;

public class AppSettingServiceImpl extends CommonServiceImpl<AppSetting> implements AppSettingService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Transactional
	@Override
	public AppSetting updateAppSetting(AppSetting appSetting) {
		return getEntityManager().merge(appSetting);
	}

	@Transactional
	@Override
	public AppSetting saveAppSetting(AppSetting appSetting) {
		getEntityManager().persist(appSetting);
		getEntityManager().flush();
		return appSetting;
	}

	@Override
	@Transactional
	public AppSetting getAppSetting(Long id) {
		AppSetting appSetting = getEntityManager().find(AppSetting.class, id);
		if (appSetting == null) {
			return saveAppSetting(new AppSetting());
		}
		return appSetting;
	}

	@Override
	public String delete(Long id) {
		return "";
	}

}
