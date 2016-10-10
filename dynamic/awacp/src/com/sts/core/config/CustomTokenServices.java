package com.sts.core.config;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

public class CustomTokenServices extends DefaultTokenServices {

	@Override
	public OAuth2Authentication loadAuthentication(String accessTokenValue) {
		OAuth2Authentication authentication = super.loadAuthentication(accessTokenValue);
		String name = authentication.getName();
		String[] parts = name.split("`");
		String userName = parts[0];

		if (StringUtils.isEmpty(userName)) {
			throw new AccessDeniedException("User principal is null");
		}
		return authentication;
	}
}
