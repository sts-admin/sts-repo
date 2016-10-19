package com.sts.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.util.SecurityEncryptor;

public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication auth = null;
		String name = authentication.getName();
		String[] parts = name.split("`");
		String userNameOrEmail = parts[0];
		String loginType = "manual";
		if (parts.length > 1) {
			loginType = parts[1];
		}
		String password = "";
		boolean verified = true;
		if ("manual".equals(loginType)) {
			password = SecurityEncryptor.encrypt(authentication.getCredentials().toString());
		} else {
			password = authentication.getCredentials().toString();
		}

		User user = userService.getUserDetail(userNameOrEmail.toLowerCase(), loginType, verified);

		// User Not Found
		if (user == null) {
			return auth;
		}

		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		String role = user.getRole().getRoleName();

		if ("manual".equals(loginType)) {
			// Compare password if provided with manual profile password
			String existingUserPassword = user.getPassword();
			if (!existingUserPassword.equals(password)) {
				throw new BadCredentialsException("unknown_password");
			}
		}

		grantedAuths.add(new CustomGrantedAuthority(role, userNameOrEmail, loginType, user.getUserCode(),
				user.getId().toString()));
		auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
