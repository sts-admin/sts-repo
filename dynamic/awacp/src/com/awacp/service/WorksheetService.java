package com.awacp.service;

import java.util.List;

import com.awacp.entity.QuoteMailTracker;
import com.awacp.entity.Worksheet;

public interface WorksheetService {

	public Worksheet updateWorksheet(Worksheet Worksheet);

	public Worksheet saveWorksheet(Worksheet Worksheet);

	public Worksheet getWorksheet(Long worksheetId);

	public Worksheet getOfficeWorksheet(Long worksheetId);

	public String delete(Long id);

	public boolean sendEmailToBidders(Long worksheetId) throws Exception;

	public QuoteMailTracker saveQuoteMailTracker(QuoteMailTracker quoteMailTracker);

	public List<QuoteMailTracker> listByTakeoff(Long takeoffId);

}
