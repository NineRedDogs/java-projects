package com.okta.examples.originexample.config;

import com.okta.examples.originexample.model.User;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


public class RequestContextUserFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(RequestContextUserFilter.class);

    @Override
    public void doFilter(
        ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.error("=====================>>>> doFilter, request : \n\n" + getReqContents(req));
        User user = RequestContextUser.findUser();
        if (user != null) {
            SecurityContextHolder.clearContext();
            Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, servletResponse);
    }


    public static String getReqContents(final HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("\n-------------------------------------------------------------------------\n\nReq:\n");
        sb.append("method: " + request.getMethod() + ", uri: " + request.getRequestURI() + "\n\n");

        request.getParameterNames();

        /** get headers */
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("   Header [" + headerName + "] :=> [" + request.getHeader(headerName) + "]\n");
        }

        /** get paramaters */
        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            sb.append("   Query Param [" + paramName + "] :=> [" + request.getParameter(paramName) + "]\n");
        }

        /** get cookies */
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                sb.append("   Cookie [" + cookie.getName() + "] :=> [" + cookie.getValue() + "]\n");
            }
        }
        sb.append("\n-------------------------------------------------------------------------\n\n");

        
        return sb.toString();
    }
}
