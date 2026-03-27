package com.samsung.azasuguesthouse.member.exception;

public class InvalidPasswordException extends RuntimeException {
    private final int tryCount;
    public InvalidPasswordException(String message, int tryCount) {
        super(message);
        this.tryCount = tryCount;
    }

    public int getTryCount() {
        return tryCount;
    }
}
