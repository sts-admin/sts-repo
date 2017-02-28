package com.awacp.service;

import com.awacp.entity.Worksheet;

public interface MailService {

	public String sendNewTakeoffMail(Long takeoffId) throws Exception;
	
	public boolean sendQuoteMailToBidders(Worksheet worksheet, String fileName, String quotePdfFilePath) throws Exception;

	public boolean sendTakeoffMail();

	public boolean sendQuoteFollowupMail(Long takeoffId) throws Exception;

	public boolean sendFinalUpdateMail();

	public boolean sendMondayReminderMail();

	public boolean sendEstimateReminderMail();

	public boolean sendAwLowInventoryMail();

	public boolean sendAwfLowInventoryMail();

	public boolean sendSbcLowInventoryMail();

	public boolean sendSplLowInventorySplMail();

	public boolean sendJobLowInventoryMail();

	public boolean sendOrderConfirmationMail();

	public boolean sendNoReplyMail();

}