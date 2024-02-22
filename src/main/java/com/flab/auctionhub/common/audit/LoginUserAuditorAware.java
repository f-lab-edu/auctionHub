package com.flab.auctionhub.common.audit;

import com.flab.auctionhub.common.exception.HttpSessionNotFoundException;
import com.flab.auctionhub.common.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Optional;

@Component // 컴포넌트 스캔을 통해 빈(Bean)으로 등록되는 클래스를 나타내는 어노테이션
public class LoginUserAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new HttpSessionNotFoundException("세션을 찾을 수 없습니다.");
        }
        String userId = SessionUtil.getLoginUserId(session);
        return Optional.of(userId);
    }
}
