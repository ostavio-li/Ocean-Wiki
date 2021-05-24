package com.carlos.ocean.security.handler;

import com.carlos.ocean.security.JSONAuthentication;
import com.carlos.ocean.security.util.JwtUtil;
import com.carlos.ocean.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Carlos Li
 * @date 2021/5/22
 */

@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        String jwt = jwtUtil.generateToken(authentication.getName());
        httpServletResponse.setHeader(jwtUtil.getHeader(), jwt);

        Result result = Result.ok().message("登陆成功");
//        httpServletResponse.setContentType("application/json; charset=UTF-8");
//        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(result));

        this.WriteJSON(httpServletRequest, httpServletResponse, result);
    }
}
