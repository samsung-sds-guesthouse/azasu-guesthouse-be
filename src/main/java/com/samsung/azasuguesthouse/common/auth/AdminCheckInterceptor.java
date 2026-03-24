package com.samsung.azasuguesthouse.common.auth;

import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.entity.member.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Member member = (Member) request.getAttribute("member");
        if (member == null || member.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("admin_only");
        }
        return true;
    }
}
