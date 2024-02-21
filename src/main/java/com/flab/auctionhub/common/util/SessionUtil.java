package com.flab.auctionhub.common.util;

import com.flab.auctionhub.user.domain.UserRoleType;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    private static final String USER_ID = "USER_ID";
    private static final String USER_ROLE = "USER_ROLE";

    private SessionUtil() {}

    /**
     * 로그인한 유저의 아이디를 세션에서 꺼낸다.
     * @param session 사용자 세션
     */
    public static String getLoginUserId(HttpSession session) {
        return (String) session.getAttribute(USER_ID);
    }
    /**
     * 로그인한 유저의 역할을 세션에서 꺼낸다.
     * @param session 사용자 세션
     */
    public static UserRoleType getLoginUserRole(HttpSession session) {
        return (UserRoleType) session.getAttribute(USER_ROLE);
    }
    /**
     * 로그인한 유저의 아이디를 세션에 저장한다.
     * @param session 사용자 세션
     */
    public static void setLoginUserId(HttpSession session, String id) {
        session.setAttribute(USER_ID, id);
    }
    /**
     * 로그인한 유저의 역할을 세션에 저장한다.
     * @param session 사용자 세션
     */
    public static void setLoginUserRole(HttpSession session, UserRoleType type) {
        session.setAttribute(USER_ROLE, type);
    }

    /**
     * 세션의 정보를 모두 삭제한다.
     * @param session 사용자 세션
     */
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
