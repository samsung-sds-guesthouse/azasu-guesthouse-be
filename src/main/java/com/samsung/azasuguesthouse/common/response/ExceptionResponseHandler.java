package com.samsung.azasuguesthouse.common.response;

import com.samsung.azasuguesthouse.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.common.exception.InvalidImageFileException;
import com.samsung.azasuguesthouse.common.auth.UnauthorizedException;
import com.samsung.azasuguesthouse.common.log.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionResponseHandler {

    // 401: 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        request.setAttribute("exception_msg", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
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
    public ResponseEntity<ExceptionResponse> handleAll(Exception e) {
        Log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse());
    }
}