package com.awacp.service;

import com.awacp.entity.Worksheet;

public interface WorksheetService {

	public Worksheet updateWorksheet(Worksheet Worksheet);

	public Worksheet saveWorksheet(Worksheet Worksheet);

	public Worksheet getWorksheet(Long WorksheetId);

	public String delete(Long id);

	public boolean sendEmailToBidders(Long worksheetId) throws Exception;

	

}
