package com.sts.core.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringCorsFilter extends DelegatingFilterProxy {
	
	public static String allowedURL="";
	
	public void setAllowedURL(String allowedURL){
		SpringCorsFilter.allowedURL = allowedURL;
	}


	  @Override
	  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException{
		  	HttpServletRequest request  = (HttpServletRequest) req;
		    HttpServletResponse response = (HttpServletResponse) res;
		    if(request.getHeader("Origin")!= null) {  
		    	allowedURL = request.getHeader("Origin"); 
		    }
		    if(!response.containsHeader(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN)){
		    	response.setHeader(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, allowedURL);
			    response.setHeader(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE");
			    response.setHeader(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS, "Accept, Origin, X-Requested-With, Content-Type, Last-Modified, Authorization");
			    response.setHeader(CorsHeaderConstants.HEADER_AC_MAX_AGE, "-1");
			    response.setHeader(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "true");
		    }
		    if(!"OPTIONS".equals(request.getMethod())) {
			   	super.doFilter(req, res, chain);
		    }
	  }
}