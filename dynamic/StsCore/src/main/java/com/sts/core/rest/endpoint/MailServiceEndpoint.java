package com.sts.core.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.mail.service.MailService;
import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.entity.PasswordResetHistory;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.web.filter.CrossOriginFilter;

public class MailServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@GET
	@Path("/sendSignUpOTPMail")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse sendSignUpOTPMail(@QueryParam("userFullName") String userFullName,
			@QueryParam("email") String email, @QueryParam("verificationCode") String verificationCode,
			@Context HttpServletResponse servletResponse) throws IOException {
		return sendSignupMail(userFullName, email, verificationCode, "OTP");
	}

	@GET
	@Path("/sendSignUpVerificationMail")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse sendSignUpVerificationMail(@QueryParam("userFullName") String userFullName,
			@QueryParam("email") String email, @QueryParam("verificationCode") String verificationCode,
			@Context HttpServletResponse servletResponse) throws IOException {
		return sendSignupMail(userFullName, email, verificationCode, "MAIL");
	}

	private StsCoreResponse sendSignupMail(String userFullName, String email, String verificationCode,
			String mailType) {
		StsCoreResponse StsCoreResponse = null;

		String emailVerificationUrl = AppPropConfig.emailVerificationUrl;
		String appName = AppPropConfig.appName;
		String projectTeamName = AppPropConfig.projectTeamName;
		String mailSubject = "Activate " + appName + " Account";
		String verificationUrl = "";

		User user = userService.getUserDetails(email);
		if (user == null) {
			StsCoreResponse = new StsCoreResponse(StsCoreConstant.USER_NOT_FOUND);
		} else {
			String event = StsCoreConstant.MANUAL_SIGNUP_EMAIL_EVENT;
			String mailTemplate = null;
			if (mailType.equals("MAIL")) {
				verificationUrl = emailVerificationUrl + verificationCode;
				mailTemplate = StsCoreConstant.SIGNUP_SUCCESS_MAIL_MESSAGE;
			} else {
				verificationUrl = "OTP= " + verificationCode;
				mailTemplate = StsCoreConstant.SIGNUP_SUCCESS_OTP_MESSAGE;
			}
			String content = String.format(mailTemplate, userFullName, appName, verificationUrl, projectTeamName);
			boolean status = mailService.sendMail(email, mailSubject, content, event);
			if (status == true) {
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_SUCCESS);
			} else {
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_FAIL);
			}
		}

		return StsCoreResponse;
	}

	@GET
	@Path("/sendPasswordResetMail")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse sendPasswordResetMail(@QueryParam("email") String email,
			@Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse StsCoreResponse = null;

		User user = userService.getUserDetails(email);
		String userName = "";
		String appName = AppPropConfig.appName;
		String projectTeamName = AppPropConfig.projectTeamName;
		String mailSubject = " OTP for " + appName + " account password reset.";
		if (user == null) {
			StsCoreResponse = new StsCoreResponse(StsCoreConstant.USER_NOT_FOUND);
		} else {
			String verificationCode = ConversionUtil.getAlphaNumeric(System.currentTimeMillis()).toUpperCase();
			userName = user.getFirstName() + "	" + user.getLastName();
			String content = String.format(StsCoreConstant.PASSWORD_RESET_MAIL_MESSAGE, userName, appName,
					verificationCode, projectTeamName);
			String event = StsCoreConstant.PASSWORD_RESET_EMAIL_EVENT;
			boolean status = mailService.sendMail(email, mailSubject, content, event);
			if (status == true) {
				PasswordResetHistory passwordResetHistory = new PasswordResetHistory();
				passwordResetHistory.setOtp(verificationCode);
				passwordResetHistory.setUser(user);
				userService.recordPasswordResetInfo(passwordResetHistory);
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_SUCCESS);
			} else {
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_FAIL);
			}
		}

		return StsCoreResponse;
	}

	@GET
	@Path("/accountVerificationMail")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse saveContact(@QueryParam("userFullName") String userFullName,
			@QueryParam("email") String email, @QueryParam("verificationCode") String verificationCode,
			@Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse StsCoreResponse = null;
		String appName = AppPropConfig.appName;
		String projectTeamName = AppPropConfig.projectTeamName;
		User user = userService.getUserDetails(email);
		if (user == null) {
			StsCoreResponse = new StsCoreResponse(StsCoreConstant.USER_NOT_FOUND);
		} else {
			String subject = "Thanks for Signing Up with " + appName + "!";
			String content = String.format(StsCoreConstant.ACCOUNT_VERIFICATION_MAIL, user.getFirstName(), appName,
					verificationCode, projectTeamName);
			String event = "Sign Up Confirmation";
			boolean status = mailService.sendMail(email, subject, content, event);
			if (status == true) {
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_SUCCESS);
			} else {
				StsCoreResponse = new StsCoreResponse(StsCoreConstant.MAIL_SEND_FAIL);
			}
		}
		return StsCoreResponse;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
