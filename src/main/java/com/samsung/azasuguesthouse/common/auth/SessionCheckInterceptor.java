package com.samsung.azasuguesthouse.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("member") == null) {
            throw new UnauthorizedException("invalid_member_session");
        }

        return true;
    }
}