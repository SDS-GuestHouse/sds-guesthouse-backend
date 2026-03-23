package com.sds_guesthouse.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid 유효성 검사 실패 시 (DTO 내부의 @Size, @NotBlank 등 위배시 @Valid 유효성 검사 실패 => MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	
    	// 서버 로그용
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            log.error("유효성 검사 실패 - 필드: [{}], 이유: [{}], 입력값: [{}]", 
                      error.getField(), 
                      error.getDefaultMessage(), 
                      error.getRejectedValue());
        });
        
        // 보안상 상세 에러는 응답에 포함시키지 않음
        return ResponseEntity.badRequest().build();
    }

    // 2. 비즈니스 로직 예외 (아이디 중복 등 직접 만든 예외)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Void> handleRuntimeExceptions(BusinessException ex) {
        log.error("비즈니스 로직 에러 발생: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }
    
    // 3. 커스텀 예외 (메시지를 명시적으로 클라이언트에게 전달해야 하는 경우)
    @ExceptionHandler(ExplicitMessageException.class)
    public ResponseEntity<Void> handleExplicitMessageException(ExplicitMessageException ex) {
        log.warn("명시적 메시지 예외 발생: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }
    
    // 4. JSON 파싱 에러 (Enum 매핑 실패, 잘못된 JSON 형식 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("JSON 파싱 실패 혹은 Enum 매핑 오류: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

//    // 5. 그 외 알 수 없는 모든 예외 (보안상 상세 내용 숨김)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
//    	log.error("서버 내부 에러 발생: ", ex); // StackTrace를 위해 ex 전체 기록
//    	
//    	Map<String, String> response = new HashMap<>();
//        response.put("message", "서버 내부 오류가 발생했습니다.");
//        
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}