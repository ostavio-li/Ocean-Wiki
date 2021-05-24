package com.carlos.ocean.security.handler;

import com.carlos.ocean.security.JSONAuthentication;
import com.carlos.ocean.vo.Result;
import com.carlos.ocean.vo.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Carlos Li
 * @date 2021/5/23
 */

@Component("myAccessDeniedHandler")
public class MyAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Result result = Result.error(ResultCode.NO_PERMISSION);
        this.WriteJSON(httpServletRequest, httpServletResponse, result);
    }
}
