package com.samsung.azasuguesthouse.common.response;

import com.samsung.azasuguesthouse.admin.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.admin.common.exception.InvalidImageFileException;
import com.samsung.azasuguesthouse.common.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역 예외 핸들러.
 *
 * <p><b>상태코드 정책: 200 / 400 / 401 만 사용</b>
 * <ul>
 *   <li>200 OK           — 정상 처리 (Controller에서 직접 반환)</li>
 *   <li>400 Bad Request  — 모든 실패 응답 (클라이언트 오류, 서버 오류 무관)</li>
 *   <li>401 Unauthorized — 인증 실패만 예외적으로 구분</li>
 * </ul>
 *
 * <p>서버 내부 오류(IO 실패 등)도 외부에는 400으로 통일합니다.
 * 실제 원인은 로그로만 확인하며 클라이언트에게 내부 구조를 노출하지 않습니다.
 */
@RestControllerAdvice
public class ExceptionResponseHandler {

    // 401
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
    }

    // 400
    @ExceptionHandler(InvalidImageFileException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidImage(InvalidImageFileException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse());
    }

    // 400
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleFileProcessing(FileProcessingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse());
    }

    // 진짜 최후의 에러 -> 400
    // Exception은 모든 에러를 포함함
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse());
    }
}
