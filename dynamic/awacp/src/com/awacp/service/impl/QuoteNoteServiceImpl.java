package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.QuoteNote;
import com.awacp.service.QuoteNoteService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class QuoteNoteServiceImpl extends CommonServiceImpl<QuoteNote>implements QuoteNoteService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<QuoteNote> listQuoteNotes(int pageNumber, int pageSize) {
		StsResponse<QuoteNote> results = listAll(pageNumber, pageSize, QuoteNote.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public QuoteNote getQuoteNote(Long id) {
		return getEntityManager().find(QuoteNote.class, id);
	}

	@Override
	@Transactional
	public QuoteNote saveQuoteNote(QuoteNote quoteNote) {
		getEntityManager().persist(quoteNote);
		getEntityManager().flush();
		return quoteNote;
	}

	@Override
	@Transactional
	public QuoteNote updateQuoteNote(QuoteNote quoteNote) {
		quoteNote = getEntityManager().merge(quoteNote);
		getEntityManager().flush();
		return quoteNote;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		QuoteNote shipTo = getQuoteNote(id);
		if (shipTo != null) {
			shipTo.setArchived(true);
			updateQuoteNote(shipTo);
			return "success";
		}
		return "fail";
	}

}
