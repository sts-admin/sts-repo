package com.awacp.service;

import com.awacp.entity.SiteSetting;

public interface SiteSettingService {

	public SiteSetting updateSiteSetting(SiteSetting SiteSetting);

	public SiteSetting saveSiteSetting(SiteSetting SiteSetting);

	public SiteSetting getSiteSetting(String viewName);

	public SiteSetting getSiteSetting(Long id);

	public String delete(Long id);

	public String setPageSizeByView(String viewName, Integer size);

}
