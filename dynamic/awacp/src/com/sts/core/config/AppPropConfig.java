package com.sts.core.config;

import org.springframework.beans.factory.annotation.Value;

public class AppPropConfig {

	public static String appBaseUrl;
	public static String resourceReadPath;
	public static String resourceWritePath;
	public static String writeUsUrl;
	public static String appName;
	public static String projectTeamName;
	public static String emailVerificationUrl;
	
	public static String emailCommonPassword;
	public static String emailNewTakeoff;
	public static String emailTakeoff;
	public static String emailQuoteFollowup;
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
	

	/**
	 * @param appBaseUrl
	 *            the appBaseUrl to set
	 */
	@Value("${appBaseUrl}")
	public void setAppBaseUrl(String appBaseUrl) {
		AppPropConfig.appBaseUrl = appBaseUrl;
	}

	/**
	 * @param appImageUrl
	 *            the appImageUrl to set
	 */
	@Value("${resourceWritePath}")
	public void setFileAccessPath(String fileAccessPath) {
		AppPropConfig.resourceWritePath = fileAccessPath;
	}
	
	/**
	 * @param appImageUrl
	 *            the appImageUrl to set
	 */
	@Value("${resourceReadPath}")
	public void setFileStoragePath(String fileStoragePath) {
		AppPropConfig.resourceReadPath = fileStoragePath;
	}

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

	/**
	 * @param emailVerificationUrl
	 *            the emailVerificationUrl to set
	 */
	@Value("${emailVerificationUrl}")
	public void setEmailVerificationUrl(String emailVerificationUrl) {
		AppPropConfig.emailVerificationUrl = emailVerificationUrl;
	}

	@Value("${resourceReadPath}")
	public void setResourceReadPath(String resourceReadPath) {
		AppPropConfig.resourceReadPath = resourceReadPath;
	}

	@Value("${resourceWritePath}")
	public void setResourceWritePath(String resourceWritePath) {
		AppPropConfig.resourceWritePath = resourceWritePath;
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
	
	

}
