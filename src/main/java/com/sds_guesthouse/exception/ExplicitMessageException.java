package com.sds_guesthouse.exception;

/**
 * 이 예외는 GlobalExceptionHandler에서 잡아서 
 * 예외 메시지를 클라이언트에게 '명시적으로' 그대로 전달합니다.
 */
public class ExplicitMessageException extends RuntimeException {
    public ExplicitMessageException(String message) {
        super(message);
    }
}