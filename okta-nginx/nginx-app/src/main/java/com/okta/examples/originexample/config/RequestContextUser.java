package com.okta.examples.originexample.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.okta.examples.originexample.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextUser {

    private static final Logger log = LoggerFactory.getLogger(RequestContextUser.class);

    public static final String HDR_EMAIL = "adm-email";
    public static final String HDR_GROUP = "adm-group";
    public static final String HDR_USERNAME = "adm-username";
    public static final String HDR_USER_ID = "adm-sub";

    public static User findUser() {
        HttpServletRequest req;
        log.error("in findUser ..........................");

        try {
            RequestAttributes reqAttr = RequestContextHolder.currentRequestAttributes();

            // int scope = RequestAttributes.SCOPE_REQUEST;
            // String[] aNames = reqAttr.getAttributeNames(scope);
            // for (String name : aNames) {
            // StringBuilder sb = new StringBuilder("SCOPE: REQUEST::: attr: ");
            // sb.append(name);
            // sb.append(" val: ");
            // sb.append(reqAttr.getAttribute(name, scope));
            // log.error(sb.toString());
            // }
            int scope = RequestAttributes.SCOPE_SESSION;
            String[] aNames = reqAttr.getAttributeNames(scope);
            for (String name : aNames) {
                StringBuilder sb = new StringBuilder("SCOPE: SESSION::: attr: ");
                sb.append(name);
                sb.append(" val: ");
                sb.append(reqAttr.getAttribute(name, scope));
                log.error(sb.toString());
            }

            String username = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(HDR_USERNAME);
            String groups = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(HDR_GROUP);
            String email = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(HDR_EMAIL);
            String userId = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(HDR_USER_ID);

            log.error("username : " + username);
            log.error("groups : " + groups);
            log.error("email : " + email);
            log.error("user id : " + userId);

            List<String> groupArray = Arrays.asList(groups.split(","));

            User user = new User(email, username, userId, groupArray);

            if (user != null) {
                req = ((ServletRequestAttributes) reqAttr).getRequest();
                req.setAttribute(User.class.getName(), user);
                return user;
            }
        } catch (Exception e) {
            log.error("Unable to resolve user", e);
        }
        log.debug("Did not find user info");
        return null;
    }
}