package com.awacp.service;

import java.util.List;

import com.awacp.entity.QuoteFollowup;
import com.awacp.entity.Takeoff;
import com.sts.core.dto.StsResponse;

public interface TakeoffService {
	public StsResponse<Takeoff> listTakeoffs(int pageNumber, int pageSize);

	public Takeoff getTakeoff(Long takeoffId);

	public String makeQuote(Long takeoffId);

	public Takeoff saveTakeoff(Takeoff takeoff) throws Exception;

	public Takeoff updateTakeoff(Takeoff takeoff);

	public void updateWorksheetInfo(Long takeoffId, Long worksheetId, Double amount);

	public String[] getNewTakeoffEmails();

	public String[] getQuoteFollowupEmails();

	public List<Takeoff> filter(String filters, int pageNumber, int pageSize);

	public List<String> listTakeoffIds(String keword);

	public List<String> listQuoteIds(String keword);

	public StsResponse<Takeoff> listNewTakeoffsForQuote(int pageNumber, int pageSize);

	public StsResponse<Takeoff> listTakeoffsForView(int pageNumber, int pageSize);

	public String delete(Long id);

	public Takeoff getTakeoffWithDetail(Long id);

	public Long saveQuoteFollowup(QuoteFollowup quoteFollowup) throws Exception;

	public List<QuoteFollowup> getAllQuoteFollowups(Long takeoffId);
}
