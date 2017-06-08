package com.awacp.service.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Bidder;
import com.awacp.entity.QuoteMailTracker;
import com.awacp.entity.QuoteNote;
import com.awacp.entity.Takeoff;
import com.awacp.entity.Worksheet;
import com.awacp.entity.WsManufacturerInfo;
import com.awacp.service.MailService;
import com.awacp.service.QuoteNoteService;
import com.awacp.service.TakeoffService;
import com.awacp.service.WorksheetService;
import com.awacp.util.QuotePdfGenerator;
import com.sts.core.config.AppPropConfig;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;

public class WorksheetServiceImpl implements WorksheetService {
	private EntityManager entityManager;

	@Autowired
	UserService userService;

	@Autowired
	TakeoffService takeoffService;

	@Autowired
	QuoteNoteService quoteNoteService;

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
		System.err.println("updateWorksheet");
		Worksheet eWorksheet = getWorksheet(worksheet.getId());
		eWorksheet.setUpdatedByUserCode(worksheet.getUpdatedByUserCode());
		eWorksheet.setSpecialNotes(worksheet.getSpecialNotes());
		eWorksheet.setGrandTotal(worksheet.getGrandTotal());
		if (worksheet.getNotes() != null) {
			if (worksheet.getNotes().isEmpty()) {
				eWorksheet.getNotes().clear();
			} else {
				Set<QuoteNote> notes = new HashSet<QuoteNote>();
				for (QuoteNote note : worksheet.getNotes()) {
					QuoteNote aNote = quoteNoteService.getQuoteNote(note.getId());
					if (aNote != null) {
						notes.add(aNote);
					}
				}
				if (!notes.isEmpty()) {
					eWorksheet.setNotes(notes);
				}
			}
		}

		if (worksheet.getManufacturerItems() == null && worksheet.getManufacturerItems().isEmpty()) {
			eWorksheet.getManufacturerItems().clear();
		} else {
			eWorksheet.setManufacturerItems(worksheet.getManufacturerItems());
		}
		getEntityManager().merge(eWorksheet);
		getEntityManager().flush();
		takeoffService.updateWorksheetInfo(worksheet.getTakeoffId(), worksheet.getId(), worksheet.getGrandTotal());
		return eWorksheet;
	}

	@Override
	@Transactional
	public Worksheet saveWorksheet(Worksheet worksheet) {
		double grandTotal = 0D;
		for (WsManufacturerInfo info : worksheet.getManufacturerItems()) {
			grandTotal = (grandTotal + (info.getQuoteAmount() == null ? 0 : info.getQuoteAmount()));
		}
		worksheet.setGrandTotal(grandTotal);
		if (worksheet.getId() != null && worksheet.getId() > 0) {
			return updateWorksheet(worksheet);
		}
		/*
		 * for (WsManufacturerInfo wsMInfo : worksheet.getManufacturerItems()) {
		 * System.err.println("Manufacturer ID: " +
		 * wsMInfo.getManufacturer().getId()); for (WsProductInfo wsProdInfo :
		 * wsMInfo.getProductItems()) { System.err.println(
		 * "Product in manufacturer block with  ID " +
		 * wsMInfo.getManufacturer().getId() + " is " +
		 * wsProdInfo.getProduct().getId()); } }
		 */
		if (worksheet.getNotes() != null) {
			if (worksheet.getNotes().isEmpty()) {
				worksheet.getNotes().clear();
			} else {
				Set<QuoteNote> notes = new HashSet<QuoteNote>();
				for (QuoteNote note : worksheet.getNotes()) {
					QuoteNote aNote = quoteNoteService.getQuoteNote(note.getId());
					if (aNote != null) {
						notes.add(aNote);
					}
				}
				if (!notes.isEmpty()) {
					worksheet.setNotes(notes);
				}
			}
		}
		getEntityManager().persist(worksheet);
		getEntityManager().flush();
		takeoffService.updateWorksheetInfo(worksheet.getTakeoffId(), worksheet.getId(), worksheet.getGrandTotal());
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
			if (entity.getManufacturerItems() != null) {
				entity.getManufacturerItems().clear();
				/*
				 * for(WsManufacturerInfo wsManu:
				 * entity.getManufacturerItems()){ if(wsManu.getPdnis() != null
				 * && !wsManu.getPdnis().isEmpty()){ wsManu.getPdnis().clear();
				 * } if(wsManu.getProductItems() != null &&
				 * !wsManu.getProductItems().isEmpty()){
				 * wsManu.getProductItems().clear(); } }
				 */
			}
			entity.setArchived(true);
			if (entity.getNotes() != null) {
				entity.getNotes().clear();
			}
			getEntityManager().merge(entity);
			getEntityManager().flush();
			getEntityManager().remove(entity);
			Takeoff takeoff = takeoffService.getTakeoff(entity.getTakeoffId());
			takeoff.setWorksheetId(null);
			takeoff.setWsCreated(false);
			takeoff.setWorksheetDeleted(true);
			takeoffService.updateTakeoff(takeoff);

			return "success";
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean sendEmailToBidders(Long worksheetId) throws Exception {
		Worksheet worksheet = getWorksheet(worksheetId);
		Takeoff takeoff = getEntityManager().find(Takeoff.class, worksheet.getTakeoffId());
		User user = userService.findUser(takeoff.getSalesPerson());
		if (user != null) {
			takeoff.setSalesPersonName(user.getFirstName() + "	" + user.getLastName());
		}
		worksheet.setTakeoff(takeoff);

		String fileName = "quote-" + Calendar.getInstance().getTimeInMillis() + ".pdf";
		String pdfFilePath = AppPropConfig.resourceWritePath + fileName;
		String logoPath = AppPropConfig.resourceWritePath + "awacp_big_logo.png";
		new QuotePdfGenerator(pdfFilePath, logoPath, worksheet).generate();
		boolean mailSendSuccess = mailService.sendQuoteMailToBidders(worksheet, fileName, pdfFilePath);
		if (mailSendSuccess) {
			if (worksheet.getTakeoff().getBidders() != null) {
				QuoteMailTracker qmt = null;
				for (Bidder bidder : worksheet.getTakeoff().getBidders()) {
					List<QuoteMailTracker> trackers = getEntityManager()
							.createNamedQuery("QuoteMailTracker.getByWsBidderAndTakeoffId")
							.setParameter("takeoffId", takeoff.getId()).setParameter("worksheetId", worksheetId)
							.getResultList();
					if (trackers != null && !trackers.isEmpty()) {
						qmt = trackers.get(0);
						qmt.setMailSentCount(qmt.getMailSentCount() + 1);
						getEntityManager().merge(qmt);
					} else {
						qmt = new QuoteMailTracker();
						qmt.setMailSentCount(1);
						qmt.setQuote("");
						qmt.setTakeoffId(takeoff.getId());
						qmt.setWorksheetId(worksheetId);
						qmt.setBidder(bidder);
						getEntityManager().persist(qmt);
					}

				}
			}
		}
		return mailSendSuccess;
	}

	@Override
	public Worksheet getOfficeWorksheet(Long worksheetId) {
		Worksheet ws = getWorksheet(worksheetId);
		ws.getNotes();
		if (ws.getTakeoff() == null) {
			ws.setTakeoff(takeoffService.getTakeoff(ws.getTakeoffId()));
		}
		if (ws.getManufacturerItems() != null) {
			for (WsManufacturerInfo wsManufacInfo : ws.getManufacturerItems()) {
				wsManufacInfo.getPdnis();
				wsManufacInfo.getProductItems();
			}
		}
		return ws;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuoteMailTracker> listByWorksheetAndTakeoff(Long worksheetId, Long takeoffId) {
		return getEntityManager().createNamedQuery("QuoteMailTracker.listByWorksheetAndTakeoffId")
				.setParameter("takeoffId", takeoffId).setParameter("worksheetId", worksheetId).getResultList();
	}

	@Override
	@Transactional
	public QuoteMailTracker saveQuoteMailTracker(QuoteMailTracker quoteMailTracker) {
		getEntityManager().persist(quoteMailTracker);
		getEntityManager().flush();
		return quoteMailTracker;
	}

}
