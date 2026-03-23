package com.samsung.azasuguesthouse.common.response;

import com.samsung.azasuguesthouse.common.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpServletResponse.SC_NOT_FOUND, "Not Found"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpServletResponse.SC_BAD_REQUEST, e.getClass().getName()));
    }
}