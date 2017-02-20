package com.sts.core.mail.service;

import java.io.File;
import java.util.List;

import com.sts.core.entity.UserMailHistory;

public interface MailService {
	public Long saveUserMailHistory(UserMailHistory userMailHistory);

	public boolean sendMail(String toAddress, String mailSubject, String content, String event) throws Exception;

	public boolean sendMail(String toAddress, String mailSubject, String content, String event, String fromAddress)
			throws Exception;

	public List<UserMailHistory> getUserMailHistory(String email, String eventName);

	public boolean sendMail(String[] toAddresses, String fromAddres, String mailSubject, String content, String event,
			String userName, String password, String fileName, File file) throws Exception;
}