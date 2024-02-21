package com.flab.auctionhub.common.util;

import com.flab.auctionhub.user.domain.UserRoleType;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionUtil {
    private static final String USER_ID = "USER_ID";
    private static final String USER_ROLE = "USER_ROLE";

    public static String getLoginUserId(HttpSession session) {
        return (String) session.getAttribute(USER_ID);
    }

    public static UserRoleType getLoginUserRole(HttpSession session) {
        return (UserRoleType) session.getAttribute(USER_ROLE);
    }

    public static void setLoginUserId(HttpSession session, String id) {
        session.setAttribute(USER_ID, id);
    }

    public static void setLoginUserRole(HttpSession session, UserRoleType type) {
        session.setAttribute(USER_ROLE, type);
    }

    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
