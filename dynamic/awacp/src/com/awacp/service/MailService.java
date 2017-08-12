package com.awacp.service;

import com.awacp.entity.Worksheet;

public interface MailService {

	public String sendNewTakeoffMail(Long takeoffId) throws Exception;

	public boolean sendQuoteMailToBidders(Worksheet worksheet, String fileName, String quotePdfFilePath)
			throws Exception;

	public boolean sendTakeoffMail();

	public boolean sendQuoteFollowupMail(Long takeoffId) throws Exception;

	public boolean sendFinalUpdateMail();

	public boolean sendMondayReminderMail();

	public boolean sendEstimateReminderMail();

	public boolean sendLowInventoryMail(String toAddress, String inv, String owner, String userCode, int orderQty,
			int availQty, int reorderQty, String size, String itemDesc, String orderNum) throws Exception;

	public boolean sendOrderConfirmationMail();

	public boolean sendNoReplyMail();

	public boolean sendPremiumOrderEmail(String emailOrId, String source, Long orderBookId) throws Exception;

}