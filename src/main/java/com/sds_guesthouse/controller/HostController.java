package com.sds_guesthouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> signin(
    		@RequestBody HostSigninRequestDto dto,
            HttpServletRequest request) {
    	
    	// 1. 비즈니스 로직 수행 (ID/PW 검증 및 엔티티 조회)
        Host host = hostService.login(dto);
        
        // 2. 세션용 DTO 생성 (메모리 절약 및 보안을 위해 필요한 정보만 추출)
        SessionUser sessionUser = SessionUser.builder()
                .id(host.getHostId())
                .name(host.getName())
                .role("ROLE_HOST") // 시큐리티 인가용 권한 (Spring 자체에서 hasRole("HOST") 호출 시 ROLE_ 접두사를 붙여서 확인하게 됨)
                .build();
        
        // 3. 시큐리티 토큰의 principal 자리에 이 DTO를 전달
        // UsernamePasswordAuthenticationToken : 스프링 시큐리티 시스템에서의 신분증 역할
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        				sessionUser, 				// 1. Principal (주체), 로그인한 유저가 누구인가? sessionUser를 넣었으므로 hostId, hostName,hostRole 등을 꺼내 쓸 수 있음
                		null, 						// 2. Credentials (자격 증명), "비밀번호" 같은 증거물, 그러나 로그인이 이미 완료된 시점에는 비밀번호를 메모리에 들고 있을 필요가 없으므로 보안상 null을 넣어 비워두는 것이 관례
                        List.of(new SimpleGrantedAuthority(sessionUser.getRole())));
        				// 3. Authorities (권한), ROLE_HOST 같은 권한 리스트를 넣어줍니다 (권한이 여러개일 수 있으므로). 시큐리티는 이 리스트를 보고 .hasRole("HOST")인 페이지에 들여보낼지 결정
        				// 스프링 시큐리티에서는 GrantedAuthority라는 인터페이스 타입으로 권한을 넣어줘야하는데, 이를 가장 단순하게 구현한 구현체가 SimpleGrantedAuthority
        
        // 4. 현재 쓰레드의 보안 컨텍스트에 인증 정보 설정
        // 이를 통해 해당 요청 내의 Service/Controller에서 즉시 인증 정보를 사용 가능하게 함
        SecurityContextHolder.	// 1. 시큐리티 저장소 관리자에게 가서
        	getContext().		// 2. 현재 내 요청 전용 보관함 (authenticationToken + 그 외 정보(접속한 사용자의 IP 주소 + JSESSIONID)를 담음)을 꺼내서 
        	setAuthentication(authenticationToken);	// 3. 그 안에 신분증을 넣음
        // 위 코드를 통해 Service, Controller 등 어디서든 현재 요청의 authenticationToken를 꺼낼 수 있게 됨.
        // SecurityContextHolder는 내부적으로 ThreadLocal 기술을 사용 (A 사용자의 요청과 B 사용자의 요청이 섞이지 않게 함)
        // 단, SecurityContextHolder에 저장하는 것은 현재 요청이 살아있는 동안에만 유지가 됨 (요청 끝나고 응답 나가면 thread 정보는 초기화)
        
        // cf) 로그인 이후의 요청에 대해서는 스프링 시큐리티 필터에서 "자동적으로" 세션을 열어서 Authentication 객체를 꺼내 SecurityContextHolder 다시 넣어준다
        // Q : 그렇다면 왜 로그인 단계에서는 왜 굳이 명시적으로 넣어줘야하는가? 
        // A : 로그인 직후 즉시 인증 상태로 만들어주기 위함
        // ex) 같은 컨트롤러에서 다른 메서드 (ex) 현재 로그인 유저의 정보 조회) 등의 메서드를 호출하는 경우, SecurityContextHolder에 아무 데이터도 없다면 미인증 사용자로 판단하고, 호출을 차단함
        
        // 4. WAS 세션 생성 및 시큐리티 컨텍스트 보관
        HttpSession session = request.getSession();
        // request.getSession()은 요청 쿠키에서 JSESSIONID을 읽고, 일치하는 세션이 있다면 WAS 메모리 세션 스코프에 일치하는 세션 객체를 읽어옴. 
        // 없다면 메모리에 새로운 세션 객체를 만들고, JSESSIONID(고유한 문자열)를 생성해서 전달
        // 또한 getSession()가 호출되는 순간 서블릿 컨테이너(Tomcat)이 client에게 보내는 모든 응답 헤더에 Set-Cookie: JSESSIONID=3EB5...; Path=/; HttpOnly 의 내용을 추가함
        // 이를 응답받은 브라우저는 쿠키 저장소에 이를 보관, 같은 서버에 요청을 보낼 때마다 헤더에 이를 실어서 보냄
        // => 결론적으로 개발자는 세션에 담을 내용(유저 pk 및 권한 등)만 데이터로 담고,
        //	  쿠키를 담는 등의 과정은 서블릿(Tomcat 등) 표준 규격에 의해 이뤄짐
        
        // session 객체 자체가 일종의 map, key - value 값을 저장
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,  // 실제값 : "SPRING_SECURITY_CONTEXT" 라는 key에 대해
                             SecurityContextHolder.getContext());	
        					// authenticationToken을 + 그 외 정보를 감싼 context를 넣어줌 (로그인 요청 응답 이후의 다른 요청들에서도 사용할 수 있도록)   

        // 5. 응답 구성
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인에 성공하였습니다.");
        response.put("hostName", host.getName());
        
        return ResponseEntity.ok(response);
    }
    

    
}