package com.awacp.service;

public interface MailService {

	public String sendNewTakeoffMail(Long takeoffId) throws Exception;

	public boolean sendTakeoffMail();

	public boolean sendQuoteFollowupMail();

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