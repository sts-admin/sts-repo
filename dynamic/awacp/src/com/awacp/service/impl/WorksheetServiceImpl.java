package com.awacp.service.impl;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Takeoff;
import com.awacp.entity.Worksheet;
import com.awacp.service.MailService;
import com.awacp.service.TakeoffService;
import com.awacp.service.WorksheetService;
import com.awacp.util.QuotePdfGenerator;
import com.sts.core.config.AppPropConfig;
import com.sts.core.service.UserService;

public class WorksheetServiceImpl implements WorksheetService {
	private EntityManager entityManager;

	@Autowired
	UserService userService;

	@Autowired
	TakeoffService takeoffService;
	
	@Autowired
	MailService mailService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public Worksheet updateWorksheet(Worksheet worksheet) {
		Worksheet eWorksheet = getWorksheet(worksheet.getId());
		eWorksheet.setUpdatedByUserCode(worksheet.getUpdatedByUserCode());
		eWorksheet.setSpecialNotes(worksheet.getSpecialNotes());
		eWorksheet.setGrandTotal(worksheet.getGrandTotal());
		if (worksheet.getNotes() == null || worksheet.getNotes().isEmpty()) {
			eWorksheet.getNotes().clear();
		}
		if (worksheet.getManufacturerItems() == null || worksheet.getManufacturerItems().isEmpty()) {
			eWorksheet.getManufacturerItems().clear();
		}
		getEntityManager().merge(eWorksheet);
		return eWorksheet;
	}

	@Override
	@Transactional
	public Worksheet saveWorksheet(Worksheet worksheet) {
		if (worksheet.getId() != null && worksheet.getId() > 0) {
			return updateWorksheet(worksheet);
		}
		getEntityManager().persist(worksheet);
		getEntityManager().flush();
		takeoffService.setWorksheetCreated(worksheet.getTakeoffId(), worksheet.getId());
		return worksheet;
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

	@Override
	public boolean sendEmailToBidders(Long worksheetId) throws Exception {
		Worksheet worksheet = getWorksheet(worksheetId);
		worksheet.setTakeoff(getEntityManager().find(Takeoff.class, worksheet.getTakeoffId()));
		String fileName = "quote-" + Calendar.getInstance().getTimeInMillis()
				+ ".pdf";
		String pdfFilePath = AppPropConfig.resourceWritePath + fileName; 
		String logoPath = AppPropConfig.resourceWritePath + "awacp_big_logo.png";
		new QuotePdfGenerator(pdfFilePath, logoPath, worksheet).generate();
		return mailService.sendQuoteMailToBidders(worksheet.getTakeoffId(), fileName, pdfFilePath);
	}

}
