package com.carlos.ocean.security.strategy;

import com.carlos.ocean.security.JSONAuthentication;
import com.carlos.ocean.vo.Result;
import com.carlos.ocean.vo.ResultCode;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Carlos Li
 * @date 2021/5/23
 */

@Component("myInvalidSessionStrategy")
public class MyInvalidSessionStrategy extends JSONAuthentication implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        this.WriteJSON(
                httpServletRequest,
                httpServletResponse,
                Result.error(ResultCode.USER_SESSION_INVALID)
        );
    }
}
