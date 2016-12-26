package com.awacp.service;

import com.awacp.entity.QuoteNote;
import com.sts.core.dto.StsResponse;

public interface QuoteNoteService {

	public QuoteNote updateQuoteNote(QuoteNote QuoteNote);

	public QuoteNote saveQuoteNote(QuoteNote QuoteNote);

	public QuoteNote getQuoteNote(Long architectId);

	public StsResponse<QuoteNote> listQuoteNotes(int pageNumber, int pageSize);

	public String delete(Long id);
}
