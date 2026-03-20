package com.sds_guesthouse.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sds_guesthouse.model.dao.AdminMapper;
import com.sds_guesthouse.model.dao.HostMapper;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;
import com.sds_guesthouse.model.entity.Host;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder; // 빈으로 등록한 BCrypt 주입
	
    @Override
    public Admin login(HostSigninRequestDto dto) {
    	Admin admin = adminMapper.findByUserId(dto.getUserId());
        // 존재하지 않거나 비밀번호 불일치 시 예외 발생
        if (admin == null || !passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
        	System.out.println(passwordEncoder.matches(dto.getPassword(), admin.getPassword()));
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."); // IllegalArgumentException 핸들러가 처리함
        }
        return admin;
    }
	
	

}
