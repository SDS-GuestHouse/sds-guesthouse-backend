package com.sds_guesthouse.model.service;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HostMapper;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostMapper hostMapper;
    private final PasswordEncoder passwordEncoder; // 빈으로 등록한 BCrypt 주입

    @Override
    @Transactional
    public void registerHost(HostSignupRequestDto dto) {
        
        // 아이디 중복 체크중복 (명시적 예외)
        if (hostMapper.existsByUserId(dto.getUserId())) {
            throw new ExplicitMessageException("이미 사용 중인 아이디입니다.");
        }
        
        // BCrypt 암호화 (Salt를 내부에서 무작위로 생성함)
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        
        Host host = Host.builder()
                .userId(dto.getUserId())
                .password(encodedPassword) // 암호화된 비번 주입
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();

        int result = hostMapper.insertHost(host);
        
        if (result == 0) {
            throw new ExplicitMessageException("회원가입 중 서버 오류가 발생했습니다.");
        }
    }
    
    @Override
    public Host login(HostSigninRequestDto dto) {
        Host host = hostMapper.findByUserId(dto.getUserId());
        // 존재하지 않거나 비밀번호 불일치 시 예외 발생
        if (host == null || !passwordEncoder.matches(dto.getPassword(), host.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."); // IllegalArgumentException 핸들러가 처리함
        }
        return host;
    }
    
    @Override
    public boolean isDuplicateId(String userId) {
        boolean isDuplicate = hostMapper.existsByUserId(userId);
        
        if (isDuplicate) {
            throw new ExplicitMessageException("이미 사용 중인 아이디입니다.");
        }
        
        return false;
    }
}