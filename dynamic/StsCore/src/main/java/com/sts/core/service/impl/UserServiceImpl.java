/**
 * 
 */
package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.Image;
import com.sts.core.entity.PasswordResetHistory;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.SecurityEncryptor;

public class UserServiceImpl implements UserService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean authenticate(String userName, String password, String loginType) {
		Query userQuery = entityManager.createNamedQuery("User." + loginType + "Login");
		userQuery.setParameter("email", userName.toLowerCase());
		userQuery.setParameter("verified", true);
		if ("manual".equals(loginType)) {
			userQuery.setParameter("userName", userName.toLowerCase());
			userQuery.setParameter("password", password);
		}
		List<User> users = userQuery.getResultList();
		return (!users.isEmpty());
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserDetails(String email, String userName) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByNameOrEmail");
		userQuery.setParameter("email", email.toLowerCase());
		userQuery.setParameter("userName", userName.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserDetails(String email) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByEmail");
		userQuery.setParameter("email", email.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	public User findUser(Long userId) {
		User user = getEntityManager().find(User.class, userId);
		setUserPhotoAndName(user);
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser() {
		Query query = getEntityManager().createNamedQuery("User.findAll");
		List<User> users = query.getResultList();
		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				setUserPhotoAndName(user);
			}
		}
		return users;
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		// Encrypt and set the password if user is trying to change the
		// password.
		// We must supply the OLD password from client side, if it is not
		// changed by user
		if (user.getPassword() != null) {
			String password = user.getPassword();
			if (SecurityEncryptor.decrypt(password) == null) {
				password = SecurityEncryptor.encrypt(password);
			}
			user.setPassword(password);
		}
		if (user.getId() == null) {
			getEntityManager().persist(user);
		} else {
			getEntityManager().merge(user);
		}
		getEntityManager().flush();
		return user;
	}

	@Override
	@Transactional
	public void removeUser(Long userId) {
		User user = findUser(userId);
		if (user != null) {
			getEntityManager().remove(user);
		}
	}

	private void setUserPhotoAndName(User user) {
		if (user.getFirstName() == null) {
			user.setFirstName(" ");
		}
		if (user.getLastName() == null) {
			user.setLastName(" ");
		}
		String photoUrl = AppPropConfig.resourceWritePath;
		user.setPhotoUrl(photoUrl + user.getAvtarImage());

		if (user.getPhoto() != null) {
			user.setPhotoUrl(photoUrl + user.getPhoto().getCreatedName() + user.getPhoto().getExtension());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserByUserNameOrEmail(String userNameOrEmail) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByNameOrEmail");
		userQuery.setParameter("email", userNameOrEmail.toLowerCase());
		userQuery.setParameter("userName", userNameOrEmail.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	public User updateUser(User user) {
		return getEntityManager().merge(user);
	}

	@Override
	public User getUserDetail(String userNameOrEmail, String loginType, boolean verified) {
		String queryHanlder = "User." + loginType + "Login";
		Query userQuery = entityManager.createNamedQuery(queryHanlder);
		userQuery.setParameter("email", userNameOrEmail.toLowerCase());
		if ("manual".equals(loginType)) {
			userQuery.setParameter("userName", userNameOrEmail.toLowerCase());
		}
		userQuery.setParameter("verified", verified);
		@SuppressWarnings("unchecked")
		List<User> users = userQuery.getResultList();

		User user = null;
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	@Transactional
	public Long recordPasswordResetInfo(PasswordResetHistory prh) {
		getEntityManager().persist(prh);
		getEntityManager().flush();
		return prh.getId();
	}

	@Override
	public boolean checkOTP(String otp) {
		Query query = entityManager.createNamedQuery("PasswordResetHistory.findByOTP");
		query.setParameter("otp", otp);
		query.setParameter("archived", false);
		@SuppressWarnings("unchecked")
		List<PasswordResetHistory> passwordResetHistorys = query.getResultList();
		return (passwordResetHistorys != null && !passwordResetHistorys.isEmpty());
	}

	@Override
	@Transactional
	public StsCoreResponse resetPassword(String email, String otp, String password) {
		StsCoreResponse status = new StsCoreResponse();
		User existingUser = getUserDetails(email);
		if (existingUser == null) {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		} else if (!existingUser.isVerified()) {
			status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
		} else {
			Query query = entityManager.createNamedQuery("PasswordResetHistory.findByOTP");
			query.setParameter("otp", otp);
			query.setParameter("archived", false);

			@SuppressWarnings("unchecked")
			List<PasswordResetHistory> passwordResetHistorys = query.getResultList();
			if (passwordResetHistorys == null || passwordResetHistorys.isEmpty()) {
				status.setStatusMessage(StsCoreConstant.NO_PASSWORD_RESET_MAIL_FOUND);
			} else {
				PasswordResetHistory passwordResetHistory = passwordResetHistorys.get(0);
				passwordResetHistory.setNewPassword(SecurityEncryptor.encrypt(password));
				passwordResetHistory.setOldPassword(existingUser.getPassword());
				passwordResetHistory.setArchived(true);
				getEntityManager().merge(passwordResetHistory);
				existingUser.setPassword(SecurityEncryptor.encrypt(password));
				getEntityManager().merge(existingUser);

				status.setStatusMessage(StsCoreConstant.PASSWORD_RESET_SUCCESS);
				status.setUserName(email);
			}
		}

		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse setPassword(String email, String otp, String password) {
		StsCoreResponse status = new StsCoreResponse();
		User existingUser = getUserDetails(email);
		if (existingUser == null) {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		} else if (!existingUser.isVerified()) {
			status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
		} else {
			if (!existingUser.getVerificationCode().equals(otp)) {
				status.setStatusMessage(StsCoreConstant.OTP_MISMATCH);
			} else {
				existingUser.setPassword(SecurityEncryptor.encrypt(password));
				getEntityManager().merge(existingUser);
				status.setStatusMessage(StsCoreConstant.PASSWORD_SET_SUCCESS);
				status.setUserName(email);
			}
		}
		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse doSignup(User user) {
		String userNameOrEmail = user.getEmail();
		if (userNameOrEmail == null || userNameOrEmail.isEmpty()) {
			userNameOrEmail = user.getUserName();
		}
		String userFullName = "";
		if (user.getFirstName() != null) {
			userFullName = userFullName + user.getFirstName();
		}
		if (user.getLastName() != null) {
			userFullName = userFullName + "\t" + user.getLastName();
		}

		String vCode = ConversionUtil.getAlphaNumeric(System.currentTimeMillis()).toUpperCase();
		user.setVerificationCode(vCode);
		user.setPassword(SecurityEncryptor.encrypt(user.getPassword().toString()));
		user.setVerified(false);

		StsCoreResponse status = new StsCoreResponse(userNameOrEmail, userFullName, vCode);

		// check user existence
		User existingUser = getUserByUserNameOrEmail(userNameOrEmail);

		Image userAvatarImage = null;
		// No user found, save user with profile detail.
		if (existingUser == null) {
			existingUser = user;
			existingUser.setAvtarImage("avatar.png");
			userAvatarImage = new Image();
			userAvatarImage.setCreatedName("avatar");
			userAvatarImage.setOriginalName("avatar");
			userAvatarImage.setExtension(".png");
			getEntityManager().persist(userAvatarImage);
			existingUser.setPhoto(userAvatarImage);
			existingUser.setPhoto(userAvatarImage);
			getEntityManager().persist(existingUser);
			getEntityManager().flush();
			status.setSignedUpUserId("" + existingUser.getId());
			status.setStatusMessage(StsCoreConstant.SIGNUP_SUCCESS);
		} else {
			status.setStatusCode(existingUser.getVerificationCode());
			boolean accountAlreadyVerified = existingUser.isVerified();
			if (accountAlreadyVerified) {
				status.setStatusMessage(StsCoreConstant.USER_ALREADY_EXISTS);
			} else {// Manually send an email to user for account activation
				status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
			}
		}
		Address address = new Address();
		address.setUserId(existingUser.getId());
		this.saveAddress(address);
		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse activateAccount(String verificationCode) {
		Query query = entityManager.createNamedQuery("User.findByVerificationCode");
		query.setParameter("verificationCode", verificationCode);
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
		StsCoreResponse status = new StsCoreResponse();
		// There should be only one record based on verification code.
		if (users != null && !users.isEmpty()) {
			if (users.size() > 1) {
				status.setStatusMessage(StsCoreConstant.MULTIPLE_ACCOUNT_FOUND);
			} else {
				User user = users.get(0);
				user.setVerified(true);
				getEntityManager().merge(user);
				getEntityManager().flush();
				status.setStatusMessage(StsCoreConstant.ACCOUNT_ACTIVATION_SUCCESS);
			}
		} else {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserDTO> searchUserByFilter(String filterString) {
		if (filterString == null || filterString.trim().length() <= 0)
			return null;
		String queryString = "SELECT u.USERID, u.USERNAME, CONCAT_WS(' ', u.FIRSTNAME, u.MIDDLENAME, u.LASTNAME) As FULLNAME FROM USER u WHERE LOWER(CONCAT_WS('\t', u.USERNAME, u.FIRSTNAME, u.MIDDLENAME, u.LASTNAME)) LIKE '%"
				+ filterString.toLowerCase() + "%'";
		Query query = getEntityManager().createNativeQuery(queryString, "User.Filter");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser(String keyword) {
		if (keyword == null || keyword.trim().length() <= 0)
			return null;
		String query = "SELECT * FROM (SELECT * FROM USER u WHERE LOWER(u.USERNAME) LIKE '" + keyword
				+ "%' UNION SELECT * FROM USER u WHERE LOWER(u.FIRSTNAME) LIKE '" + keyword
				+ "%') AS a ORDER BY a.CREATIONDATE DESC";
		return getEntityManager().createNativeQuery(query, User.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Address getAddress(Long userId) {
		List<Address> address = getEntityManager().createNamedQuery("Address.findByUserId")
				.setParameter("userId", userId).getResultList();
		return address != null && !address.isEmpty() ? address.get(0) : null;
	}

	@Override
	@Transactional
	public Address saveAddress(Address address) {
		if (address.getId() == null) {
			getEntityManager().persist(address);
			getEntityManager().flush();
		} else {
			getEntityManager().merge(address);
		}
		return address;
	}

}
