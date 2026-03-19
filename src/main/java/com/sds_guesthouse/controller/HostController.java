package com.sds_guesthouse.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.model.dto.HostSignupRequestDto;
import com.sds_guesthouse.model.service.HostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/host") // 공통 경로 설정
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;

    /**
     * 호스트 회원가입 API
     * POST /api/v1/host/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody HostSignupRequestDto dto) {
        // 1. 유효성 검사(@Valid) 통과 후 서비스 호출
//        hostService.registerHost(dto);
        
        // 2. 성공 응답 반환
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body("회원가입이 성공적으로 완료되었습니다.");
    }
}