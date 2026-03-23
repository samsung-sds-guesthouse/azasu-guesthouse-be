package com.samsung.azasuguesthouse.common.log;

import com.samsung.azasuguesthouse.entity.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long memberId = 0;
        if (request.getAttribute("member") != null && request.getAttribute("member") instanceof Member member) {
            memberId = member.getId();
        }

        long elapsedTime = -1;
        if (request.getAttribute("req_start_time") != null && request.getAttribute("req_start_time") instanceof Instant start) {
            elapsedTime = Instant.now().toEpochMilli() - start.toEpochMilli();
        }

        Log.request(
                response.getStatus(),
                memberId,
                ex == null ? "-" : ex.getMessage(),
                request.getRequestURI(),
                request.getParameterMap(),
                elapsedTime
        );
    }
}