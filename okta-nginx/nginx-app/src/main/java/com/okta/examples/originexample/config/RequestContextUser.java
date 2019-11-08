package com.okta.examples.originexample.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.examples.originexample.model.User;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextUser {

    private static final Logger log = LoggerFactory.getLogger(RequestContextUser.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static final String USER_HEADER = "x-userinfo";
    public static final String AUTH_HDR = "authorization";
    public static final String USERNAME_HDR = "username";

    public static User findUser() {
        String userInfoHeader;
        HttpServletRequest req;

        log.error("in findUser ..........................");

        try {
            RequestAttributes reqAttr = RequestContextHolder.currentRequestAttributes();

            int scope = RequestAttributes.SCOPE_REQUEST;
            String[] aNames = reqAttr.getAttributeNames(scope);
            for (String name : aNames) {
                StringBuilder sb = new StringBuilder("SCOPE: REQUEST::: attr: ");
                sb.append(name);
                sb.append(" val: ");
                sb.append(reqAttr.getAttribute(name, scope));
                log.error(sb.toString());
            }
            scope = RequestAttributes.SCOPE_SESSION;
            aNames = reqAttr.getAttributeNames(scope);
            for (String name : aNames) {
                StringBuilder sb = new StringBuilder("SCOPE: SESSION::: attr: ");
                sb.append(name);
                sb.append(" val: ");
                sb.append(reqAttr.getAttribute(name, scope));
                log.error(sb.toString());
            }

            log.error("reqAttr class : " + reqAttr.getClass().getName());
            log.error("reqAttr getReq : " + ((ServletRequestAttributes) reqAttr).getRequest());
            log.error("user header : " + ((ServletRequestAttributes) reqAttr).getRequest().getHeader(USER_HEADER));
            final String authHdr = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(AUTH_HDR);
            log.error("auth header : " + ((ServletRequestAttributes) reqAttr).getRequest().getHeader(AUTH_HDR));

            final String usernameHdr = ((ServletRequestAttributes) reqAttr).getRequest().getHeader(USERNAME_HDR);
            log.error("username header : " + ((ServletRequestAttributes) reqAttr).getRequest().getHeader(USERNAME_HDR));

            User user2FromAuth = decodeAndExtractUsername(usernameHdr);


            User userFromAuth = decodeAndExtractUserData(authHdr);
            if (userFromAuth != null) {
                req = ((ServletRequestAttributes) reqAttr).getRequest();
                req.setAttribute(User.class.getName(), userFromAuth);
                return userFromAuth;
            }
        } catch (Exception e) {
            log.error("Unable to resolve user from {} header", USER_HEADER, e);
        }
        log.debug("Did not find user from {} header.", USER_HEADER);
        return null;
    }

    private static User decodeAndExtractUserData(String authHdr) {
        log.error("[[[ in decodeAndExtractUserData ..... ]]]]");

        // 1. strip bearer text
        final String bearer = "Bearer ";
        // final String clientSecret = "XaewflSG_nlmMm07971BHd69Qj-fTrqdQXtN2xwd";
        log.error("Auth hdr : " + authHdr);
        // Bearer eyJraWQiOiJOdFl5al9uYkhUREdxOVo2c2dVaS1mS

        if (authHdr != null && authHdr.startsWith(bearer)) {
            final String strippedAuthHdr = authHdr.substring(bearer.length());
            log.error("(Stripped) Auth hdr : " + strippedAuthHdr);

            // 2. decode and create user object
            User user = decodeJwt(strippedAuthHdr);
            return user;
        }
        return null;
    }

    private static User decodeAndExtractUsername(String unHdr) {
        log.error("[[[ in decodeAndExtractUsername ..... ]]]]");

        // final String clientSecret = "XaewflSG_nlmMm07971BHd69Qj-fTrqdQXtN2xwd";
        log.error("username hdr : " + unHdr);

        if (unHdr != null) {

            // 1. decode and create user object
            User user = decodeJwt(unHdr);
            return user;
        }
        return null;
    }

    public static User decodeJwt(String jwtEncoded) {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer("https://dev-424995.okta.com/oauth2/default").setAudience("api://default") // defaults to
                                                                                                      // 'api://default'
                .setConnectionTimeout(Duration.ofSeconds(1)) // defaults to 1s
                .setReadTimeout(Duration.ofSeconds(1)) // defaults to 1s
                .build();

        try {
            log.error("AJG=1==========================");
            Jwt jwt = jwtVerifier.decode(jwtEncoded);
            Map<String, Object> claims = jwt.getClaims();
            log.error(Arrays.toString(claims.entrySet().toArray()));
            log.error("AJG=2==========================");

            final String keyID = "sub";
            final String keyFirstName = "user.firstName";
            final String keyLastName = "user.lastName";
            final String keyEmail = "user.email";
            final String keyGroups = "groups";

            String id = (String) claims.get(keyID);
            log.error("ID : " + id);

            String firstName = (String) claims.get(keyFirstName);
            String lastName = (String) claims.get(keyLastName);
            log.error("Full name : [" + firstName + "_" + lastName + "]");

            String email = (String) claims.get(keyEmail);
            log.error("email : " + email);

            List<String> groups = (List<String>) claims.get(keyGroups);
            StringBuilder sb = new StringBuilder("Groups: [");
            for (String g : groups) {
                sb.append(g + ",");
            }
            sb.append("] numGroups : " + groups.size());

            log.error("groups : " + sb.toString());

            User u = new User(email, firstName + " " + lastName, id, groups);
            return u;
        } catch (JwtVerificationException e) {
            log.error("Caught JwtVerificationException when decoding jwt, e:" + e.getMessage());
        }
        return null;
    }

}