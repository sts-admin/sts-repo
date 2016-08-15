/**
 * 
 */
package com.sts.core.mail.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import com.sts.core.config.AppPropConfig;
import com.sts.core.entity.UserMailHistory;
import com.sts.core.mail.service.MailService;

public class MailServiceImpl implements MailService {

	private static Logger logger = Logger.getLogger(MailServiceImpl.class);

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	@Override
	@Transactional
	public Long saveUserMailHistory(UserMailHistory userMailHistory) {
		getEntityManager().persist(userMailHistory);
		return userMailHistory.getId();
	}

	@Override
	@Transactional
	public boolean sendMail(String toAddress, String mailSubject, String content, String event) {
		return sendMail(toAddress, mailSubject, content, event, AppPropConfig.writeUsUrl);
	}

	@Override
	@Transactional
	public boolean sendMail(String toAddress, String mailSubject, String content, String event, String fromAddress) {
		logger.info("Email being send to " + toAddress + ", from " + fromAddress);
		UserMailHistory userMailHistory = new UserMailHistory(toAddress, content, event);
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		boolean mailSendSuccess = false;
		msg.setTo(toAddress);
		msg.setFrom(fromAddress);
		msg.setSubject(mailSubject);
		msg.setText(content);
		try {

			this.mailSender.send(msg);
			this.saveUserMailHistory(userMailHistory);
			mailSendSuccess = true;

		} catch (MailException ex) {
			ex.printStackTrace();
		}
		logger.info("Email sent to " + toAddress);
		return mailSendSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<UserMailHistory> getUserMailHistory(String email, String eventName) {
		Query mailHistoryQuery = entityManager.createNamedQuery("UserMailHistory.findByUserEmailAndEvent");
		mailHistoryQuery.setParameter("email", email.toLowerCase());
		mailHistoryQuery.setParameter("eventName", eventName);
		return mailHistoryQuery.getResultList();
	}

}
