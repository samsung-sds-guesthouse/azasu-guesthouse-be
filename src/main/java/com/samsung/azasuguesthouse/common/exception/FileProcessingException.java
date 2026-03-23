package com.samsung.azasuguesthouse.common.exception;

/**
 * 파일 읽기/쓰기 중 IO 오류가 발생했을 때 발생하는 예외.
 */
public class FileProcessingException extends RuntimeException {

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}