package com.flab.auctionhub.common.interceptor;

import com.flab.auctionhub.common.exception.PermissionDeniedException;
import com.flab.auctionhub.common.exception.UnauthenticatedException;
import com.flab.auctionhub.common.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        HttpSession session = request.getSession();

        if (session == null || SessionUtil.getLoginUserId(session) == null) {
            throw new UnauthenticatedException("미인증된 사용자 입니다.");
        }

        if (SessionUtil.getLoginUserRole(session) == null) {
            throw new PermissionDeniedException("사용자 권한이 없습니다.");
        }
        return true;
    }
}
