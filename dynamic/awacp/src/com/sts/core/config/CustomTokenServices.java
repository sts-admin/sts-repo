package com.sts.core.config;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.sts.core.entity.User;
import com.sts.core.service.UserService;

public class CustomTokenServices extends DefaultTokenServices {

	@Autowired
	private UserService userService;

	@Override
	public OAuth2Authentication loadAuthentication(String accessTokenValue) {
		OAuth2Authentication authentication = super.loadAuthentication(accessTokenValue);
		String name = authentication.getName();
		String[] parts = name.split("`");
		String userName = parts[0];
		String loginType = "manual";
		if (parts.length > 1) {
			loginType = parts[1];
		}

		if (StringUtils.isEmpty(userName)) {
			throw new AccessDeniedException("User principal is null");
		}
		User user = null;
		if ("manual".equals(loginType)) {
			user = userService.getUserByUserNameOrEmail(userName);
		} else {
			user = userService.getUserDetails(userName);
		}
		return authentication;
	}
}
