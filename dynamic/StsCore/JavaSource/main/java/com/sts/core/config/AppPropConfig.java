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

}
