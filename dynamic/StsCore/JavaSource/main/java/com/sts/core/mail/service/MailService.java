package com.sts.core.mail.service;

import java.util.List;

import com.sts.core.entity.UserMailHistory;
public interface MailService {
	public Long saveUserMailHistory(UserMailHistory userMailHistory);

	/**
	 * Return email send status, true on success, false otherwise
	 * 
	 * @param toAddress
	 *            Target user email address to home mail will be sent
	 * @param content
	 *            Mail message content
	 * @param event
	 *            Event source or cause of sending email.
	 * @param mailSubject
	 *            Email subject
	 * 
	 * @return Return email send status, true on success, false otherwise
	 */
	public boolean sendMail(String toAddress, String mailSubject, String content, String event);

	/**
	 * Return email send status, true on success, false otherwise
	 * 
	 * @param toAddress
	 *            Target user email address to home mail will be sent
	 * @param mailSubject
	 *            Email subject
	 * @param content
	 *            Mail message content
	 * @param event
	 *            Event source or cause of sending email.
	 * @param fromAddress
	 *            Source user email address to home mail will be sent
	 * 
	 * @return Return email send status, true on success, false otherwise
	 */
	public boolean sendMail(String toAddress, String mailSubject, String content, String event, String fromAddress);

	/**
	 * Return List of UserMailHistory if found based on user email and event
	 * type, null otherwise
	 * 
	 * @param email
	 *            Email id of the user based on which mail history will be
	 *            searched.
	 * @param eventName
	 *            Name of the event caused sending mail
	 * @return List of UserMailHistory if found based on user email and event
	 *         type, null otherwise
	 */
	public List<UserMailHistory> getUserMailHistory(String email, String eventName);

}