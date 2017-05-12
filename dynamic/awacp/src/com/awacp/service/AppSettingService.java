package com.awacp.service;

import com.awacp.entity.AppSetting;

public interface AppSettingService {

	public AppSetting updateAppSetting(AppSetting appSetting);

	public AppSetting saveAppSetting(AppSetting appSetting);

	public AppSetting getAppSetting(Long id);

	public String delete(Long id);

}
