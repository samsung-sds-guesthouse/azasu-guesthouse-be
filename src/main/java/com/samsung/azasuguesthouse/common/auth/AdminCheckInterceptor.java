package com.samsung.azasuguesthouse.common.auth;

import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.entity.member.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoResourceFoundException {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("member") == null
                || !(session.getAttribute("member") instanceof Member member)
                || member.getRole() != Role.ADMIN
        ) {
            throw new NoResourceFoundException(HttpMethod.valueOf(request.getMethod()), request.getRequestURI(), request.getServletPath());
        }
        return true;
    }
}
