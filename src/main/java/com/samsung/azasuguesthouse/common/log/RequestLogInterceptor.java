package com.samsung.azasuguesthouse.common.log;

import com.samsung.azasuguesthouse.entity.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long memberId = 0;
        HttpSession session = request.getSession(false);

        if (session != null
                && session.getAttribute("member") != null
                && session.getAttribute("member") instanceof Member member
        ) {
            memberId = member.getId();
        }

        long elapsedTime = -1;
        if (request.getAttribute("req_start_time") != null
                && request.getAttribute("req_start_time") instanceof Instant start
        ) {
            elapsedTime = Instant.now().toEpochMilli() - start.toEpochMilli();
        }

        String msg = "-";
        if (ex != null) {
            msg = ex.getMessage();
        } else if (request.getAttribute("exception_msg") != null
                && request.getAttribute("exception_msg") instanceof String exceptionMsg
        ) {
            msg = exceptionMsg;
        }

        Log.request(
                response.getStatus(),
                memberId,
                msg,
                request.getMethod(),
                request.getRequestURI(),
                request.getParameterMap(),
                elapsedTime
        );
    }
}