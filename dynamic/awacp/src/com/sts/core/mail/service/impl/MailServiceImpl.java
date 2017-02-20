/**
 * 
 */
package com.sts.core.mail.service.impl;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import com.sts.core.config.AppPropConfig;
import com.sts.core.entity.UserMailHistory;
import com.sts.core.mail.service.MailService;

public class MailServiceImpl implements MailService {

	private static Logger logger = Logger.getLogger(MailServiceImpl.class);

	//private MailSender mailSender;
	// private SimpleMailMessage templateMessage;
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	/*
	 * public void setMailSender(MailSender mailSender) { this.mailSender =
	 * mailSender; }
	 * 
	 * public void setTemplateMessage(SimpleMailMessage templateMessage) {
	 * this.templateMessage = templateMessage; }
	 */

	@Override
	@Transactional
	public Long saveUserMailHistory(UserMailHistory userMailHistory) {
		getEntityManager().persist(userMailHistory);
		return userMailHistory.getId();
	}

	@Override
	@Transactional
	public boolean sendMail(String toAddress, String mailSubject, String content, String event) throws Exception {
		return sendMail(toAddress, mailSubject, content, event, AppPropConfig.writeUsUrl);
	}

	@Override
	@Transactional
	public boolean sendMail(String toAddress, String mailSubject, String content, String event, String fromAddress)
			throws Exception {
		String[] addresses = { toAddress };
		return sendMail(addresses, fromAddress, mailSubject, content, event, AppPropConfig.emailNewTakeoff,
				AppPropConfig.emailCommonPassword, null, null);
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

	@Override
	public boolean sendMail(String[] toAddresses, String fromAddress, String mailSubject, String content, String event,
			String userName, String password, String fileName, File file) throws Exception {
		logger.info("Email being send to " + toAddresses + ", from " + fromAddress);
		// UserMailHistory userMailHistory = new UserMailHistory(toAddresses,
		// content, event);
		JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", true);
		javaMailProperties.put("mail.smtp.quitwait", false);
		javaMailProperties.put("mail.debug", true);
		//emailSender.setHost("smtp.gmail.com");
		emailSender.setHost("mail.skilledsoftech.com");
		emailSender.setPort(587);
		emailSender.setProtocol("smtp");
		emailSender.setJavaMailProperties(javaMailProperties);
		emailSender.setUsername(userName);
		emailSender.setPassword(password);
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		mimeMessage.setContent(content, "text/html");
		
		

		helper.setFrom(fromAddress);
		helper.setTo(toAddresses);
		helper.setSubject(mailSubject);
		
		//set attachment if any
		if(file != null){
			String attachmentFileName = fileName != null && !fileName.isEmpty()?fileName: file.getName();
			helper.addAttachment(attachmentFileName, file);
		}
		
		mimeMessage.setFrom(fromAddress);

		boolean mailSendSuccess = false;
		try {
			emailSender.send(mimeMessage);
			// this.saveUserMailHistory(userMailHistory);
			mailSendSuccess = true;

		} catch (MailException ex) {
			ex.printStackTrace();
		}
		logger.info("Email sent to " + toAddresses);
		return mailSendSuccess;
	}

}
