import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid 유효성 검사 실패 시 (DTO 내부의 @Size, @NotBlank 등 위배시 @Valid 유효성 검사 실패 => MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	
    	// 서버 로그에는 상세한 이유를 남깁니다. (개발자용)
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            log.error("유효성 검사 실패 - 필드: [{}], 이유: [{}], 입력값: [{}]", 
                      error.getField(), 
                      error.getDefaultMessage(), 
                      error.getRejectedValue());
        });
        
    	// DTO 내부의 @NotBlank(message="...")가 무엇이든 상관없이 무시합니다.
        Map<String, String> response = new HashMap<>();
        response.put("message", "입력 정보가 올바르지 않거나 처리할 수 없는 요청입니다.");
        
        // 보안상 상세 에러(errors 필드)는 아예 담지 않고 리턴합니다.
        return ResponseEntity.badRequest().body(response);
    }

    // 2. 비즈니스 로직 예외 (아이디 중복 등 직접 만든 예외)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(IllegalArgumentException ex) {
    	// 1. 서버 로그에는 상세한 이유를 남깁니다. (개발자용)
        log.error("비즈니스 로직 에러 발생: {}", ex.getMessage());

        // 2. 클라이언트에게는 보안을 위해 메시지를 통일함
        Map<String, String> response = new HashMap<>();
        response.put("message", "입력 정보가 올바르지 않거나 처리할 수 없는 요청입니다.");
        
        return ResponseEntity.badRequest().body(response);
    }

    // 3. 그 외 알 수 없는 모든 예외 (보안상 상세 내용 숨김)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
    	log.error("서버 내부 에러 발생: ", ex); // StackTrace를 위해 ex 전체 기록
    	
    	Map<String, String> response = new HashMap<>();
        response.put("message", "서버 내부 오류가 발생했습니다.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}