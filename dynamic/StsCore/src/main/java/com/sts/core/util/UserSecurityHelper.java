package com.sts.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSecurityHelper {
    
	public static String getLoggedInUserName(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
	}
}
