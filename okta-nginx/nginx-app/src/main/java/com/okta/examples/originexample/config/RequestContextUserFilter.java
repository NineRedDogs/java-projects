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
        log.error("=====================>>>> AJG-2 : " + getReqContents(req));
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
        StringBuilder sb = new StringBuilder("Req: ");

        log.error("====AJG-1");
        sb.append("method: " + request.getMethod() + ", uri: " + request.getRequestURI());

        request.getParameterNames();

        Cookie[] cookies = request.getCookies();
        log.error("====AJG-2 - cookies : " + cookies);

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                sb.append("cookie - " + cookie.getName() + ", Value - " + cookie.getValue() + "\n");
            }
        }
        log.error("====AJG-3");

        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            sb.append("Param Name - " + paramName + ", Value - " + request.getParameter(paramName) + "\n");
        }
        log.error("====AJG-4a");        
        
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("Header Name - " + headerName + ", Value - " + request.getHeader(headerName) + "\n");
        }
        log.error("====AJG-4b");


        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            sb.append("Parameter Name - " + paramName + ", Value - " + request.getParameter(paramName) + "\n");
        }
        log.error("====AJG-5");

        return sb.toString();
    }
}
