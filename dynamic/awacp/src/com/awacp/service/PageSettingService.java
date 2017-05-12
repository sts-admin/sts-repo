package com.awacp.service;

import com.awacp.entity.PageSetting;

public interface PageSettingService {

	public PageSetting updateSiteSetting(PageSetting PageSetting);

	public PageSetting saveSiteSetting(PageSetting PageSetting);

	public PageSetting getSiteSetting(String viewName);

	public PageSetting getSiteSetting(Long id);

	public String delete(Long id);

	public String setPageSizeByView(String viewName, Integer size);

}
