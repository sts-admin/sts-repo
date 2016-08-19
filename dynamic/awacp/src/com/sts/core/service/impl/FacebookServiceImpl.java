package com.sts.core.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.entity.FacebookProfile;
import com.sts.core.entity.Image;
import com.sts.core.entity.User;
import com.sts.core.entity.UserType;
import com.sts.core.service.FacebookService;
import com.sts.core.service.UserService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.FileUtils;

public class FacebookServiceImpl implements FacebookService {

	@Autowired
	private UserService userService;

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsCoreResponse getProfileDetail(String accessToken) {
		StsCoreResponse status = new StsCoreResponse();
		Facebook facebook = new FacebookTemplate(accessToken);
		if (facebook.isAuthorized()) {
			status.setStatusMessage("success");
			status.setUserName(facebook.userOperations().getUserProfile().getEmail());
			status.setSignedUpUserId(facebook.userOperations().getUserProfile().getId());
		} else {
			status.setStatusMessage("failure");
		}
		return status;
	}

	private User setUserDetail(Facebook facebook, User user) {
		if (user == null) {
			user = new User();
		}
		user.setEmail(facebook.userOperations().getUserProfile().getEmail());
		user.setFirstName(facebook.userOperations().getUserProfile().getFirstName());
		user.setMiddleName(facebook.userOperations().getUserProfile().getMiddleName());
		user.setLastName(facebook.userOperations().getUserProfile().getLastName());
		user.setGender(facebook.userOperations().getUserProfile().getGender());
		user.setUserName(facebook.userOperations().getUserProfile().getEmail());

		return user;
	}

	private FacebookProfile setProfileDetail(Facebook facebook, FacebookProfile profile) {
		if (profile == null) {
			profile = new FacebookProfile();
		}
		profile.setEmail(facebook.userOperations().getUserProfile().getEmail());
		profile.setUserName(facebook.userOperations().getUserProfile().getEmail());
		profile.setVerified(facebook.userOperations().getUserProfile().isVerified());
		profile.setFacebookId(facebook.userOperations().getUserProfile().getId());
		return profile;
	}

	@Override
	@Transactional
	public StsCoreResponse setupProfile(String accessToken, String userType) throws FileNotFoundException, IOException {
		StsCoreResponse status = new StsCoreResponse();
		Facebook facebook = new FacebookTemplate(accessToken);
		if (facebook.isAuthorized()) {
			String code = ConversionUtil.getAlphaNumeric(System.currentTimeMillis()).toUpperCase();
			String email = facebook.userOperations().getUserProfile().getEmail();
			// check if facebook profile exists
			FacebookProfile profile = getProfile(email);
			User user = null;
			if (profile == null) {
				profile = setProfileDetail(facebook, profile);
				user = setUserDetail(facebook, user);
				user.setType(UserType.valueOf(userType));
			} else {
				user = userService.getUserDetails(profile.getEmail());
			}
			// update image detail always.
			Image photo = null;
			profile.setFacebookToken(accessToken);
			if (profile.getPhoto() == null) {
				photo = new Image();
			} else {
				photo = profile.getPhoto();
			}
			photo.setCreatedName(code);
			photo.setExtension(".png");
			photo.setOriginalName("facebookProfilePhoto");
			if (profile.getPhoto() == null) {
				getEntityManager().persist(photo);
			} else {
				getEntityManager().merge(photo);
			}
			user.setPhoto(photo);
			profile.setPhoto(photo);

			if (user.getId() == null) {
				getEntityManager().persist(user);
			} else {
				getEntityManager().merge(user);
			}
			getEntityManager().flush();

			profile.setUserId(user.getId());

			if (profile.getId() == null) {
				getEntityManager().persist(profile);
				status.setStatusMessage(StsCoreConstant.PROFILE_CREATE_SUCCESS);
			} else {
				getEntityManager().merge(profile);
				status.setStatusMessage(StsCoreConstant.PROFILE_MERGE_SUCCESS);
			}

			status.setUserName(profile.getEmail());
			status.setSignedUpUserId(profile.getFacebookId());
			FileUtils.writeBinaryFileContent(AppPropConfig.resourceWritePath, code + ".png",
					facebook.userOperations().getUserProfileImage());

		} else {
			status.setStatusMessage(StsCoreConstant.USER_UNAUTHORIZED);
		}
		return status;

	}

	@Override
	@SuppressWarnings("unchecked")
	public FacebookProfile getProfile(String email) {
		List<FacebookProfile> profiles = getEntityManager().createNamedQuery("FacebookProfile.findByEmail")
				.setParameter("email", email).getResultList();
		return profiles == null || profiles.isEmpty() ? null : profiles.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FacebookProfile getProfileByUserId(Long userId) {
		List<FacebookProfile> profiles = getEntityManager().createNamedQuery("FacebookProfile.findByUserId")
				.setParameter("userId", userId).getResultList();
		return profiles == null || profiles.isEmpty() ? null : profiles.get(0);
	}

}
