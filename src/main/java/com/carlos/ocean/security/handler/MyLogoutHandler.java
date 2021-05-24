package com.carlos.ocean.security.handler;

import com.carlos.ocean.security.JSONAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Carlos Li
 * @date 2021/5/23
 */

@Component("myLogoutHandler")
public class MyLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println("Logout Token: " + token);
        System.out.println("Logout Method: " + httpServletRequest.getMethod());
        if (token != null && token.length() > 0) {
            SecurityContextHolder.clearContext();
        }
    }
}
