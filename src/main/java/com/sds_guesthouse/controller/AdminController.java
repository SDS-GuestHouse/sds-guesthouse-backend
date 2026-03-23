package com.sds_guesthouse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.model.dto.admin.AdminHouseManageRequestDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.service.AdminService;
import com.sds_guesthouse.security.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
    /**
     * admin 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(
    		@RequestBody HostSigninRequestDto dto,
            HttpServletRequest request) {
        Admin admin = adminService.login(dto);
        
        if (admin == null) {
        	ResponseEntity.badRequest().build();
        }
        
        SessionUser sessionUser = SessionUser.builder()
                .id(admin.getAdminId())
                .name(admin.getName())
                .role("ROLE_ADMIN")
                .build();
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // authenticationToken : 일종의 신분증
        				sessionUser, 				// Principal (주체) : 로그인한 유저가 누구인가? sessionUser를 넣었으므로 hostId, hostName,hostRole 등을 꺼내 쓸 수 있음
                		null, 						// Credentials (자격 증명) : "비밀번호" 같은 증거물, 그러나 로그인이 이미 완료된 시점에는 비밀번호를 메모리에 들고 있을 필요가 없으므로 보안상 null을 넣어 비워두는 것이 관례
                        List.of(new SimpleGrantedAuthority(sessionUser.getRole()))); // Authorities (권한) : ROLE_HOST 같은 권한 리스트를 넣어줍니다
        
        SecurityContextHolder.	// 시큐리티 저장소 관리자 (현재 쓰레드의 보안 컨텍스트에 인증 정보 설정)
        	getContext().		// 현재 내 요청 전용 보관함 (authenticationToken + 그 외 정보(접속한 사용자의 IP 주소 + JSESSIONID)를 담음) 
        	setAuthentication(authenticationToken);	// 그 안에 신분증을 넣음
        
        HttpSession session = request.getSession(); // WAS 세션 생성 및 시큐리티 컨텍스트 보관
        
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,  // 실제값 : "SPRING_SECURITY_CONTEXT" 라는 key에 대해
                             SecurityContextHolder.getContext());// authenticationToken을 + 그 외 정보를 감싼 context를 넣어줌 (로그인 요청 응답 이후의 다른 요청들에서도 사용할 수 있도록)   

        
        return ResponseEntity.ok().build();
    }
    
    /**
     * 상태별 숙소 관리 목록 조회 (승인 대기, 삭제 대기 등)
     */
    @GetMapping("/manage")
    public ResponseEntity<List<House>> getPendingHouses(
            @RequestBody AdminHouseManageRequestDto dto) {
        
        // 서비스에서 해당 status를 가진 숙소 리스트를 가져옴
        List<House> houses = adminService.getHousesByStatus(dto.getStatus());
        
        return ResponseEntity.ok(houses);
    }

}
