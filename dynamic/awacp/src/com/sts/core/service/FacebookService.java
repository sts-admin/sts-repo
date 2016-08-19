package com.sts.core.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.sts.core.dto.StsCoreResponse;
import com.sts.core.entity.FacebookProfile;

public interface FacebookService {
	public FacebookProfile getProfile(String email);
	public FacebookProfile getProfileByUserId(Long userId);
	public StsCoreResponse getProfileDetail(String accessToken);
	public StsCoreResponse setupProfile(String accessToken, String userType) throws FileNotFoundException, IOException;
}