package com.sts.core.config;

import org.springframework.beans.factory.annotation.Value;

public class AppPropConfig {

	public static String asBaseUrl; // Application service base URL
	public static String acBaseUrl; // Application client base URL
	public static String acImageDir; // Application client image directory
	public static String acResourceWriteDir; // Application client directory to
												// write content / files etc
	public static String acResourceDir; // Resource directory name in client
										// application

	public static String acImageLocalUrl;

	public static String writeUsUrl;
	public static String appName;
	public static String projectTeamName;

	public static String emailCommonPassword;
	public static String emailNewTakeoff;
	public static String emailTakeoff;
	public static String emailQuoteFollowup;
	public static String emailQuoteFollowupPassword;
	public static String quoteMailToBidder;
	public static String quoteMailToBidderPassword;
	public static String emailFinalUpdate;
	public static String emailMondayReminder;
	public static String emailDeleteInvoice;
	public static String emailEstimateReminder;
	public static String emailLowInventoryAW;
	public static String emailLowInventoryAWF;
	public static String emailLowInventorySBC;
	public static String emailLowinventorySPL;
	public static String emailLowinventoryJ;
	public static String emailOrderConfirmation;
	public static String emailNoReply;

	public static String emailPremiumOrder;
	public static String emailPremiumOrderPassword;

	/**
	 * @param writeUsUrl
	 *            the writeUsUrl to set
	 */
	@Value("${writeUsUrl}")
	public void setWriteUsUrl(String writeUsUrl) {
		AppPropConfig.writeUsUrl = writeUsUrl;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	@Value("${appName}")
	public void setAppName(String appName) {
		AppPropConfig.appName = appName;
	}

	/**
	 * @param projectTeamName
	 *            the projectTeamName to set
	 */
	@Value("${projectTeamName}")
	public void setProjectTeamName(String projectTeamName) {
		AppPropConfig.projectTeamName = projectTeamName;
	}

	@Value("${emailCommonPassword}")
	public void setEmailCommonPassword(String emailCommonPassword) {
		AppPropConfig.emailCommonPassword = emailCommonPassword;
	}

	@Value("${emailNewTakeoff}")
	public void setEmailNewTakeoff(String emailNewTakeoff) {
		AppPropConfig.emailNewTakeoff = emailNewTakeoff;
	}

	@Value("${emailTakeoff}")
	public void setEmailTakeoff(String emailTakeoff) {
		AppPropConfig.emailTakeoff = emailTakeoff;
	}

	@Value("${emailQuoteFollowup}")
	public void setEmailQuoteFollowup(String emailQuoteFollowup) {
		AppPropConfig.emailQuoteFollowup = emailQuoteFollowup;
	}

	@Value("${emailFinalUpdate}")
	public void setEmailFinalUpdate(String emailFinalUpdate) {
		AppPropConfig.emailFinalUpdate = emailFinalUpdate;
	}

	@Value("${emailMondayReminder}")
	public void setEmailMondayReminder(String emailMondayReminder) {
		AppPropConfig.emailMondayReminder = emailMondayReminder;
	}

	@Value("${emailDeleteInvoice}")
	public void setEmailDeleteInvoice(String emailDeleteInvoice) {
		AppPropConfig.emailDeleteInvoice = emailDeleteInvoice;
	}

	@Value("${emailEstimateReminder}")
	public void setEmailEstimateReminder(String emailEstimateReminder) {
		AppPropConfig.emailEstimateReminder = emailEstimateReminder;
	}

	@Value("${emailLowInventoryAW}")
	public void setEmailLowInventoryAW(String emailLowInventoryAW) {
		AppPropConfig.emailLowInventoryAW = emailLowInventoryAW;
	}

	@Value("${emailLowInventoryAWF}")
	public void setEmailLowInventoryAWF(String emailLowInventoryAWF) {
		AppPropConfig.emailLowInventoryAWF = emailLowInventoryAWF;
	}

	@Value("${emailLowInventorySBC}")
	public void setEmailLowInventorySBC(String emailLowInventorySBC) {
		AppPropConfig.emailLowInventorySBC = emailLowInventorySBC;
	}

	@Value("${emailLowinventorySPL}")
	public void setEmailLowinventorySPL(String emailLowinventorySPL) {
		AppPropConfig.emailLowinventorySPL = emailLowinventorySPL;
	}

	@Value("${emailLowinventoryJ}")
	public void setEmailLowinventoryJ(String emailLowinventoryJ) {
		AppPropConfig.emailLowinventoryJ = emailLowinventoryJ;
	}

	@Value("${emailOrderConfirmation}")
	public void setEmailOrderConfirmation(String emailOrderConfirmation) {
		AppPropConfig.emailOrderConfirmation = emailOrderConfirmation;
	}

	@Value("${emailNoReply}")
	public void setEmailNoReply(String emailNoReply) {
		AppPropConfig.emailNoReply = emailNoReply;
	}

	@Value("${quoteMailToBidder}")
	public void setQuoteMailToBidder(String quoteMailToBidder) {
		AppPropConfig.quoteMailToBidder = quoteMailToBidder;
	}

	@Value("${quoteMailToBidderPassword}")
	public void setQuoteMailToBidderPassword(String quoteMailToBidderPassword) {
		AppPropConfig.quoteMailToBidderPassword = quoteMailToBidderPassword;
	}

	@Value("${emailQuoteFollowupPassword}")
	public void setEmailQuoteFollowupPassword(String emailQuoteFollowupPassword) {
		AppPropConfig.emailQuoteFollowupPassword = emailQuoteFollowupPassword;
	}

	@Value("${emailPremiumOrder}")
	public void setEmailPremiumOrder(String emailPremiumOrder) {
		AppPropConfig.emailPremiumOrder = emailPremiumOrder;
	}

	@Value("${emailPremiumOrderPassword}")
	public void setEmailPremiumOrderPassword(String emailPremiumOrderPassword) {
		AppPropConfig.emailPremiumOrderPassword = emailPremiumOrderPassword;
	}

	@Value("${asBaseUrl}")
	public void setAsBaseUrl(String asBaseUrl) {
		AppPropConfig.asBaseUrl = asBaseUrl;
	}

	@Value("${acBaseUrl}")
	public void setAcBaseUrl(String acBaseUrl) {
		AppPropConfig.acBaseUrl = acBaseUrl;
	}

	@Value("${acImageDir}")
	public void setAcImageDir(String acImageDir) {
		AppPropConfig.acImageDir = acImageDir;
	}

	@Value("${acResourceWriteDir}")
	public void setAcResourceWriteDir(String acResourceWriteDir) {
		AppPropConfig.acResourceWriteDir = acResourceWriteDir;
	}

	@Value("${acResourceDir}")
	public void setAcResourceDir(String acResourceDir) {
		AppPropConfig.acResourceDir = acResourceDir;
	}

	@Value("${acImageLocalUrl}")
	public void setAcImageLocalUrl(String acImageLocalUrl) {
		AppPropConfig.acImageLocalUrl = acImageLocalUrl;
	}

}
