package com.awacp.service;

import java.util.List;

import com.awacp.entity.QuoteFollowup;
import com.awacp.entity.Takeoff;
import com.sts.core.dto.AutoComplete;
import com.sts.core.dto.StsResponse;

public interface TakeoffService {
	public StsResponse<Takeoff> listTakeoffs(int pageNumber, int pageSize, String redOrGreenOrAll);

	public Takeoff getTakeoff(Long takeoffId);

	public String makeQuote(Long takeoffId);

	public Takeoff saveTakeoff(Takeoff takeoff) throws Exception;

	public Takeoff updateTakeoff(Takeoff takeoff);

	public void updateWorksheetInfo(Long takeoffId, Long worksheetId, Double amount);

	public String[] getNewTakeoffEmails();

	public String[] getQuoteFollowupEmails();

	public List<Takeoff> filter(String filters, int pageNumber, int pageSize);

	public AutoComplete autoCompleteList(String keyword, String field);

	public StsResponse<Takeoff> listNewTakeoffsForQuote(int pageNumber, int pageSize);

	public StsResponse<Takeoff> listTakeoffsForView(int pageNumber, int pageSize, boolean quoteView);

	public String delete(Long id);

	public Takeoff getTakeoffWithDetail(Long id);

	public Long saveQuoteFollowup(QuoteFollowup quoteFollowup) throws Exception;

	public List<QuoteFollowup> getAllQuoteFollowups(Long takeoffId);

	public Takeoff getTakeoffByQuoteId(String quoteId);

	public StsResponse<Takeoff> generateTakeoffReport(Takeoff takeoff);

	public void setQuotePdfGenerated(Long takeoffId, String pdfFilePath);

	public StsResponse<Takeoff> searchTakeoffs(Takeoff takeoff);
	
	public int totalRecordsForTheYear(String recordType);

}
