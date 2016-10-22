package com.sts.core.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class LogoutHandler implements LogoutSuccessHandler {

	private TokenStore tokenStore;

	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String tokens = request.getHeader("Authorization");
		if (tokens != null && !tokens.isEmpty()) {
			if (tokens.indexOf(" ") >= 0) {
				String value = tokens.substring(tokens.indexOf(" ")).trim();
				((JdbcTokenStore) tokenStore).removeAccessToken(value);				
			}
			response.getOutputStream().write("You have successfully logged out.".getBytes());
		}

	}
}
