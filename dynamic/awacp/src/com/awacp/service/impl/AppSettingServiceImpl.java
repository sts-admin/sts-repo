package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.MenuItem;
import com.awacp.entity.SiteColor;
import com.awacp.entity.SiteEmailAccount;
import com.awacp.entity.SiteInfo;
import com.awacp.entity.SystemLog;
import com.awacp.service.AppSettingService;
import com.sts.core.config.AppPropConfig;
import com.sts.core.dto.StsResponse;

public class AppSettingServiceImpl implements AppSettingService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	private SiteInfo setSiteInfo(SiteInfo source, SiteInfo target) {

		target.setCompanyName(source.getCompanyName());
		target.setFrontWebsite(source.getFrontWebsite());
		target.setBackendApp(target.getBackendApp());
		target.setOfficeAddress(source.getOfficeAddress());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setFaxNumber(source.getFaxNumber());
		target.setEmailAddress(source.getEmailAddress());
		target.setClockOneLabel(source.getClockOneLabel());
		target.setClockOneActive(source.isClockOneActive());
		target.setClockOneTimezone(source.getClockOneTimezone());

		target.setClockTwoLabel(source.getClockTwoLabel());
		target.setClockTwoActive(source.isClockTwoActive());
		target.setClockTwoTimezone(source.getClockTwoTimezone());

		target.setSmtpHost(source.getSmtpHost());
		target.setSmtpPort(source.getSmtpPort());

		target.setLoginLogo(source.getLoginLogo());
		target.setHeaderLogo(source.getHeaderLogo());
		target.setEmailLogo(source.getEmailLogo());

		target.setLoginLogoUrl(source.getLoginLogoUrl());
		target.setHeaderLogoUrl(source.getHeaderLogoUrl());
		target.setEmailLogoUrl(source.getEmailLogoUrl());

		return target;
	}

	@Transactional
	@Override
	public SiteInfo saveSiteInfo(SiteInfo si) {
		if (si.getId() != null && si.getId() > 0) {
			getEntityManager().merge(si);
		} else {
			SiteInfo eSi = getSiteInfo();
			if (eSi == null) {
				getEntityManager().persist(si);
			} else {
				setSiteInfo(si, eSi);
				getEntityManager().merge(eSi);
			}

		}
		getEntityManager().flush();
		return si;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SiteInfo getSiteInfo() {
		List<SiteInfo> items = getEntityManager().createNamedQuery("SiteInfo.findAll").getResultList();
		SiteInfo si = null;
		if (items != null && !items.isEmpty()) {
			si = items.get(0);
			if (si.getLoginLogo() != null) {
				si.setLoginLogoUrl(AppPropConfig.acBaseUrl + "/" + AppPropConfig.acResourceDir + "/"
						+ si.getLoginLogo().getCreatedName() + si.getLoginLogo().getExtension());
			}
			if (si.getHeaderLogo() != null) {
				si.setHeaderLogoUrl(AppPropConfig.acBaseUrl + "/" + AppPropConfig.acResourceDir + "/"
						+ si.getHeaderLogo().getCreatedName() + si.getHeaderLogo().getExtension());
			}
			if (si.getEmailLogo() != null) {
				si.setEmailLogoUrl(AppPropConfig.acBaseUrl + "/" + AppPropConfig.acResourceDir + "/"
						+ si.getEmailLogo().getCreatedName() + si.getEmailLogo().getExtension());
			}
		}
		return si;
	}

	@Override
	public SiteInfo getSiteInfo(Long id) {
		return getEntityManager().find(SiteInfo.class, id);
	}

	@Transactional
	@Override
	public String deleteSiteInfo(Long id) {
		SiteInfo entity = getSiteInfo(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Transactional
	@Override
	public SiteColor saveSiteColor(SiteColor sc) {
		if (sc.getId() != null) {
			getEntityManager().merge(sc);
		} else {
			getEntityManager().persist(sc);
		}
		getEntityManager().flush();
		return sc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SiteColor getSiteColor() {
		List<SiteColor> items = getEntityManager().createNamedQuery("SiteColor.findAll").getResultList();
		return items != null && !items.isEmpty() ? items.get(0) : null;
	}

	@Override
	public SiteColor getSiteColor(Long id) {
		return getEntityManager().find(SiteColor.class, id);
	}

	@Transactional
	@Override
	public String deleteSiteColor(Long id) {
		SiteColor entity = getSiteColor(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Transactional
	@Override
	public SiteEmailAccount saveSiteEmailAccount(SiteEmailAccount sea) {
		if (sea.getId() != null) {
			getEntityManager().merge(sea);
		} else {
			getEntityManager().persist(sea);
		}
		getEntityManager().flush();
		return sea;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SiteEmailAccount getSiteEmailAccount() {
		List<SiteEmailAccount> items = getEntityManager().createNamedQuery("SiteEmailAccount.findAll").getResultList();
		return items != null && !items.isEmpty() ? items.get(0) : null;
	}

	@Override
	public SiteEmailAccount getSiteEmailAccount(Long id) {
		return getEntityManager().find(SiteEmailAccount.class, id);
	}

	@Transactional
	@Override
	public String deleteSiteEmailAccount(Long id) {
		SiteEmailAccount entity = getSiteEmailAccount(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Transactional
	@Override
	public MenuItem saveSiteMenuItem(MenuItem ma) {
		if (ma.getId() != null) {
			getEntityManager().merge(ma);
		} else {
			getEntityManager().persist(ma);
		}
		getEntityManager().flush();
		return ma;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuItem getSiteMenuItem() {
		List<MenuItem> items = getEntityManager().createNamedQuery("MenuItem.findAll").getResultList();
		return items != null && !items.isEmpty() ? items.get(0) : null;
	}

	@Override
	public MenuItem getSiteMenuItem(Long id) {
		return getEntityManager().find(MenuItem.class, id);
	}

	@Transactional
	@Override
	public String deleteSiteMenuItem(Long id) {
		MenuItem entity = getSiteMenuItem(id);
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
	public StsResponse<SystemLog> listSystemLogs(int pageNumber, int pageSize) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(entity.id").append(") FROM ")
				.append(SystemLog.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		int count = -1;

		Object object = getEntityManager().createQuery(sb.toString()).getSingleResult();
		if (object != null) {
			count = (((Long) object).intValue());
		}

		StsResponse<SystemLog> response = new StsResponse<SystemLog>();
		if (pageNumber <= 1) {
			response.setTotalCount(count);
		}
		StringBuffer sb2 = new StringBuffer("SELECT entity FROM ").append(SystemLog.class.getSimpleName())
				.append(" entity WHERE entity.archived = 'false' ORDER BY entity.dateCreated DESC");
		Query query = getEntityManager().createQuery(sb2.toString());
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<SystemLog> results = query.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(results);
	}

	@Transactional
	@Override
	public SystemLog saveSystemLog(SystemLog sl) {
		SystemLog test = new SystemLog();
		test.setSection("TEST");
		test.setDescription("TEST");
		getEntityManager().merge(test);
		return test;
	}

	@Override
	public StsResponse<SystemLog> filterLogs(SystemLog log) {
		StringBuffer selectQueryBuffer = new StringBuffer("SELECT sl FROM ").append(SystemLog.class.getSimpleName())
				.append(" sl WHERE sl.archived = 'false' ");

		StringBuffer countQueryBuffer = new StringBuffer("SELECT COUNT(sl.id) FROM ")
				.append(SystemLog.class.getSimpleName()).append(" sl WHERE sl.archived = 'false' ");

		if (log.getFromDate() != null) {
			selectQueryBuffer.append(
					" AND (FUNC('DATE', sl.dateCreated) >= :fromDate OR FUNC('DATE', sl.dateUpdated) >= :fromDate) ");
			countQueryBuffer.append(
					" AND (FUNC('DATE', sl.dateCreated) >= :fromDate OR FUNC('DATE', sl.dateUpdated) >= :fromDate) ");
		}

		if (log.getToDate() != null) {
			selectQueryBuffer.append(
					" AND (FUNC('DATE', sl.dateCreated) <= :toDate OR FUNC('DATE', sl.dateUpdated) <= :toDate) ");
			countQueryBuffer.append(
					" AND (FUNC('DATE', sl.dateCreated) <= :toDate OR FUNC('DATE', sl.dateUpdated) <= :toDate) ");
		}

		if (log.getUc() != null && !log.getUc().isEmpty()) {
			selectQueryBuffer.append(" AND LOWER(sl.uc) =:uc");
			countQueryBuffer.append(" AND LOWER(sl.uc) =:uc");
		}

		if (log.getSection() != null && !log.getSection().isEmpty()) {
			selectQueryBuffer.append(" AND LOWER(sl.section) =:section");
			countQueryBuffer.append(" AND LOWER(sl.section) =:section");
		}

		if (log.getDescription() != null && !log.getDescription().isEmpty()) {
			selectQueryBuffer.append(" AND LOWER(sl.description) LIKE :description");
			countQueryBuffer.append(" AND LOWER(sl.description) LIKE :description");
		}

		Query selectQuery = getEntityManager().createQuery(selectQueryBuffer.toString());
		Query countQuery = getEntityManager().createQuery(countQueryBuffer.toString());

		if (log.getFromDate() != null) {
			selectQuery.setParameter("fromDate", log.getFromDate().getTime(), TemporalType.DATE);
			countQuery.setParameter("fromDate", log.getFromDate().getTime(), TemporalType.DATE);
		}
		if (log.getToDate() != null) {
			selectQuery.setParameter("toDate", log.getToDate().getTime(), TemporalType.DATE);
			countQuery.setParameter("toDate", log.getToDate().getTime(), TemporalType.DATE);
		}

		if (log.getUc() != null && !log.getUc().isEmpty()) {
			selectQuery.setParameter("uc", log.getUc().toLowerCase());
			countQuery.setParameter("uc", log.getUc().toLowerCase());
		}

		if (log.getSection() != null && !log.getSection().isEmpty()) {
			selectQuery.setParameter("section", log.getSection().toLowerCase());
			countQuery.setParameter("section", log.getSection().toLowerCase());
		}

		if (log.getDescription() != null && !log.getDescription().isEmpty()) {
			selectQuery.setParameter("description", "%" + log.getDescription().toLowerCase() + "%");
			countQuery.setParameter("description", "%" + log.getDescription().toLowerCase() + "%");
		}

		StsResponse<SystemLog> response = new StsResponse<SystemLog>();
		if (log.getPageNumber() <= 1) {
			Object object = countQuery.getSingleResult();
			int count = 0;
			if (object != null) {
				count = (((Long) object).intValue());
			}
			response.setTotalCount(count);
		}
		if (log.getPageNumber() > 0 && log.getPageSize() > 0) {
			selectQuery.setFirstResult(((log.getPageNumber() - 1) * log.getPageSize())).setMaxResults(log.getPageSize());
		}
		@SuppressWarnings("unchecked")
		List<SystemLog> results = selectQuery.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(results);
	}

}
