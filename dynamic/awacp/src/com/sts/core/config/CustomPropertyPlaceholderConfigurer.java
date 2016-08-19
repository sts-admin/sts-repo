package com.sts.core.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.sts.core.util.SecurityEncryptor;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	@Override
	protected String resolvePlaceholder(String placeholder, java.util.Properties properties) {
		String propertyValue = super.resolvePlaceholder(placeholder, properties);
		if(placeholder != null && placeholder.indexOf("password") > 0) {
			propertyValue = SecurityEncryptor.decrypt(propertyValue);
		}
		return propertyValue;
	}
}
