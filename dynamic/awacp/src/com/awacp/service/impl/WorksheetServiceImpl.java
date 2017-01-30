package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Worksheet;
import com.awacp.service.WorksheetService;
import com.sts.core.service.UserService;

public class WorksheetServiceImpl implements WorksheetService {
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
	@Transactional
	public Worksheet updateWorksheet(Worksheet Worksheet){
		return null;
	}

	@Override
	@Transactional
	public Worksheet saveWorksheet(Worksheet worksheet){
		if(worksheet.getId() != null && worksheet.getId() > 0){
			return updateWorksheet(worksheet);
		}
		String[] manufacturers = worksheet.getManufacturerArray();
		if(manufacturers != null && manufacturers.length > 0){
			for(String mIdString: manufacturers){
				Long mId = Long.valueOf(mIdString);
			}
		}
		return null;
	}

	@Override
	public Worksheet getWorksheet(Long worksheetId) {
		return getEntityManager().find(Worksheet.class, worksheetId);
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Worksheet entity = getWorksheet(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
