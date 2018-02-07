package com.awacp.service;

import com.awacp.entity.MenuItem;
import com.awacp.entity.SiteColor;
import com.awacp.entity.SiteEmailAccount;
import com.awacp.entity.SiteInfo;
import com.awacp.entity.SystemLog;
import com.sts.core.dto.StsResponse;

public interface AppSettingService {

	// Site Info
	public SiteInfo saveSiteInfo(SiteInfo si);

	public SiteInfo getSiteInfo();

	public SiteInfo getSiteInfo(Long id);

	public String deleteSiteInfo(Long id);

	// Color Info
	public SiteColor saveSiteColor(SiteColor sc);

	public SiteColor getSiteColor();

	public SiteColor getSiteColor(Long id);

	public String deleteSiteColor(Long id);

	// Email Account Info
	public SiteEmailAccount saveSiteEmailAccount(SiteEmailAccount sea);

	public SiteEmailAccount getSiteEmailAccount();

	public SiteEmailAccount getSiteEmailAccount(Long id);

	public String deleteSiteEmailAccount(Long id);

	// Menu Item
	public MenuItem saveSiteMenuItem(MenuItem ma);

	public MenuItem getSiteMenuItem();

	public MenuItem getSiteMenuItem(Long id);

	public String deleteSiteMenuItem(Long id);

	public SystemLog saveSystemLog(SystemLog sl);

	public StsResponse<SystemLog> listSystemLogs(int pageNumber, int pageSize);

	public StsResponse<SystemLog> filterLogs(SystemLog log);

}
