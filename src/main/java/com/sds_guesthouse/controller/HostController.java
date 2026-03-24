package com.sds_guesthouse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.model.dto.host.HostIdDuplicateCheckRequestDto;
import com.sds_guesthouse.model.dto.host.HostIdDuplicateCheckResponseDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;
import com.sds_guesthouse.model.service.HostService;
import com.sds_guesthouse.util.auth.SecurityLoginService;
import com.sds_guesthouse.util.auth.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/host") // 공통 경로 설정
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;
    private final SecurityLoginService securityLoginService;

    /**
     * 호스트 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody HostSignupRequestDto dto) { // @Valid : dto의 유효성 검사 수행
        hostService.registerHost(dto);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 호스트 회원가입시 아이디 중복 체크
     */
    @PostMapping("/check")
    public ResponseEntity<HostIdDuplicateCheckResponseDto> checkDuplicateId(@Valid @RequestBody HostIdDuplicateCheckRequestDto request) {
    	
    	return ResponseEntity.ok(hostService.isDuplicateId(request.getUserId()));
    }
    
    /**
     * 호스트 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(
    		@Valid @RequestBody HostSigninRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response) {
    	
    	SessionUser sessionUser = hostService.login(dto);
    	securityLoginService.login(sessionUser, request, response);
	    return ResponseEntity.ok().build();
        
    }
    

    
}