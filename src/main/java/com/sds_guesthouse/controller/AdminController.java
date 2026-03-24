package com.sds_guesthouse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.model.dto.admin.AdminHouseManageRequestDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.service.AdminService;
import com.sds_guesthouse.util.auth.SecurityLoginService;
import com.sds_guesthouse.util.auth.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	private final SecurityLoginService securityLoginService;
	
    /**
     * admin 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(
    		@RequestBody HostSigninRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response) {
    	
    	SessionUser sessionUser = adminService.login(dto);
    	securityLoginService.login(sessionUser, request, response);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 상태별 숙소 관리 목록 조회 (승인 대기, 삭제 대기 등)
     */
    @PostMapping("/manage")
    public ResponseEntity<List<House>> getPendingHouses(
            @Valid @RequestBody AdminHouseManageRequestDto dto) {
        
        // 서비스에서 해당 status를 가진 숙소 리스트를 가져옴
        List<House> houses = adminService.getHousesByStatus(dto.getStatus());
        return ResponseEntity.ok(houses);
    }
    
    /**
     * 특정 숙소의 상태 변경 (승인/거절 등)
     * PUT /api/v1/admin/houses/{id}
     */
    @PutMapping("/houses/{id}")
    public ResponseEntity<Void> updateHouseStatus(
            @PathVariable("id") long houseId,
            @Valid @RequestBody AdminHouseManageRequestDto dto) {
        
        adminService.updateHouseStatus(houseId, dto.getStatus());
        
        return ResponseEntity.ok().build();
    }

}
