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
import com.sds_guesthouse.security.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<Void> signup(@Valid @RequestBody HostSignupRequestDto dto) { // @Valid : dto의 유효성 검사 수행
        hostService.registerHost(dto);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 호스트 회원가입시 아이디 중복 체크
     */
    @PostMapping("/check")
    public ResponseEntity<HostIdDuplicateCheckResponseDto> checkDuplicateId(@RequestBody HostIdDuplicateCheckRequestDto request) {
    	
    	if (hostService.isDuplicateId(request.getUserId())) {
        	return ResponseEntity.ok(HostIdDuplicateCheckResponseDto.builder()
        			.available(true)
        			.build());
        }
    	return ResponseEntity.ok(HostIdDuplicateCheckResponseDto.builder()
    			.available(false)
    			.build());
    }
    
    /**
     * 호스트 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(
    		@RequestBody HostSigninRequestDto dto,
            HttpServletRequest request) { // JSESSIONID 읽기 위해 HttpServletRequest request 파라미터에 추가
        Host host = hostService.login(dto);
        
        SessionUser sessionUser = SessionUser.builder() // 세션용 DTO 생성 (메모리 절약 및 보안을 위해 필요한 정보만 추출)
                .id(host.getHostId())
                .name(host.getName())
                .role("ROLE_HOST") // 시큐리티 인가용 권한 (Spring 자체에서 hasRole("HOST") 호출 시 ROLE_ 접두사를 붙여서 확인하게 됨)
                .build();
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // authenticationToken : 일종의 신분증
        				sessionUser, 				// Principal (주체) : 로그인한 유저가 누구인가? sessionUser를 넣었으므로 hostId, hostName,hostRole 등을 꺼내 쓸 수 있음
                		null, 						// Credentials (자격 증명) : "비밀번호" 같은 증거물, 그러나 로그인이 이미 완료된 시점에는 비밀번호를 메모리에 들고 있을 필요가 없으므로 보안상 null을 넣어 비워두는 것이 관례
                        List.of(new SimpleGrantedAuthority(sessionUser.getRole()))); // Authorities (권한) : ROLE_HOST 같은 권한 리스트를 넣어줍니다
        
        // 현재 쓰레드의 보안 컨텍스트에 인증 정보 설정
        SecurityContextHolder.	// 시큐리티 저장소 관리자
        	getContext().		// 현재 내 요청 전용 보관함 (authenticationToken + 그 외 정보(접속한 사용자의 IP 주소 + JSESSIONID)를 담음) 
        	setAuthentication(authenticationToken);	// 그 안에 신분증을 넣음
        
        // WAS 세션 생성 및 시큐리티 컨텍스트 보관
        // request.getSession()은 요청 쿠키에서 JSESSIONID을 읽고, 일치하는 세션이 있다면 WAS 메모리 세션 스코프에 일치하는 세션 객체를 읽어옴. 없다면 메모리에 새로운 세션 객체를 만들고, JSESSIONID(고유한 문자열)를 생성해서 전달
        HttpSession session = request.getSession();
        
        // session 객체 자체가 일종의 map, key - value 값을 저장
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,  // 실제값 : "SPRING_SECURITY_CONTEXT" 라는 key에 대해
                             SecurityContextHolder.getContext());// authenticationToken을 + 그 외 정보를 감싼 context를 넣어줌 (로그인 요청 응답 이후의 다른 요청들에서도 사용할 수 있도록)   
        
        return ResponseEntity.ok().build();
    }
    

    
}