package com.samsung.azasuguesthouse.common.response;

import com.samsung.azasuguesthouse.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.common.exception.InvalidImageFileException;
import com.samsung.azasuguesthouse.common.auth.UnauthorizedException;
import com.samsung.azasuguesthouse.common.log.Log;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.exception.LoginUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionResponseHandler {

    // 404: Not Found
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(HttpServletResponse.SC_NOT_FOUND, "Not Found"));
    }

    // 401: 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED"));
    }

    // 422: 틀린 패스워드 입력
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPasswordException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage() + " [tryCount=" + e.getTryCount() + "]");
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Wrong Password", Map.of("try_count", e.getTryCount())));
    }

    // 422: 로그인 시도 횟수 초과
    @ExceptionHandler(LoginUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleLoginUnavailableException(LoginUnavailableException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Login Unavailable"));
    }

    // 400: 검증 오류 (컨트롤러의 @Valid 실패 시 호출됨)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException e) {
        Log.warn("Validation failed: " + e.getBindingResult().getAllErrors());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(400, "Validation failed"));
    }

    // 400: 이미지 형식 오류
    @ExceptionHandler(InvalidImageFileException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidImage(InvalidImageFileException e) {
        Log.error("Invalid image file: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse());
    }

    // 400: 파일 처리 실패
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleFileProcessing(FileProcessingException e) {
        Log.error("File processing failed", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse());
    }

    // 400: 기타 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleElse(Exception e, HttpServletRequest request) {
        String exceptionMsg = e.getMessage();
        Log.error(exceptionMsg, e);
        request.setAttribute("exception_msg", exceptionMsg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpServletResponse.SC_BAD_REQUEST, "FAIL"));
    }
}