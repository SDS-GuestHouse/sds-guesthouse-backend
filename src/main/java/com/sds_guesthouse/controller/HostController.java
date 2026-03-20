package com.sds_guesthouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dto.host.HostIdDuplicateCheckRequestDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;
import com.sds_guesthouse.model.service.HostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/host") // 공통 경로 설정
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;

    /**
     * 호스트 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody HostSignupRequestDto dto) {
        // 1. 유효성 검사(@Valid) 통과 후 서비스 호출
        hostService.registerHost(dto);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입이 성공적으로 완료되었습니다.");
        
        // 2. 성공 응답 반환
        return ResponseEntity.ok(response);
    }
    
    /**
     * 호스트 회원가입시 아이디 중복 체크
     */
    @PostMapping("/check")
    public ResponseEntity<Map<String, String>> checkDuplicateId(@RequestBody HostIdDuplicateCheckRequestDto dto) {
        // 중복이면 서비스 내부에서 ExplicitMessageException이 터짐
        hostService.isDuplicateId(dto.getUserId());
        
        // 예외가 안 터졌다면 사용 가능한 아이디라는 뜻
        Map<String, String> response = new HashMap<>();
        response.put("message", "사용 가능한 아이디입니다.");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 호스트 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signin(@RequestBody HostSigninRequestDto dto) {
        // 로그인 실패 시 여기서 예외가 터져서 핸들러로 이동함
        Host host = hostService.login(dto);
        
        // 여기까지 왔다면 로그인 성공!
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인에 성공하였습니다.");
        response.put("hostName", host.getName()); // 사용자 이름 정도는 응답에 포함 가능
        
        return ResponseEntity.ok(response);
    }
    

    
}