package com.sts.core.web.filter;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;

@CrossOriginResourceSharing(allowAllOrigins=true, allowHeaders={"Accept, Origin, X-Requested-With, Content-Type, Last-Modified"}, exposeHeaders={"Jykra-Service"}, allowCredentials=true, maxAge=-1)
public class CrossOriginFilter {

    @Context
    private HttpHeaders headers;

    @OPTIONS
    @Path("/")
    @LocalPreflight
    public Response options() {
        String origin = headers.getRequestHeader("Origin").get(0);
        return Response.ok()
           .header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "GET POST DELETE PUT OPTIONS")
           .header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "true")
           .header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, origin)
           .header(CorsHeaderConstants.HEADER_AC_EXPOSE_HEADERS, "Jykra-Service")
           .header(CorsHeaderConstants.HEADER_AC_MAX_AGE, -1)
           .header(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS, "Accept, Origin, X-Requested-With, Content-Type, Last-Modified")
           .build();
    }
}
