package com.samsung.azasuguesthouse.common.response;

import com.samsung.azasuguesthouse.common.auth.UnauthorizedException;
import com.samsung.azasuguesthouse.common.log.Log;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.exception.LoginUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpServletResponse.SC_NOT_FOUND, "Not Found"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPasswordException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage() + " [tryCount=" + e.getTryCount() + "]");
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Wrong Password", Map.of("try_count", e.getTryCount())));
    }

    @ExceptionHandler(LoginUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleLoginUnavailableException(LoginUnavailableException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Login Unavailable"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, HttpServletRequest request) {
        String exceptionMsg = e.getMessage();
        Log.error(exceptionMsg, e);
        request.setAttribute("exception_msg", exceptionMsg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpServletResponse.SC_BAD_REQUEST, "FAIL"));
    }
}