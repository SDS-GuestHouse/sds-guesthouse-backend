package com.sds_guesthouse.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sds_guesthouse.exception.BusinessException;
import com.sds_guesthouse.model.dao.HostMapper;
import com.sds_guesthouse.model.dto.host.HostIdDuplicateCheckResponseDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostMapper hostMapper;
    private final PasswordEncoder passwordEncoder; // 빈으로 등록한 BCrypt 주입

    @Override
    public void registerHost(HostSignupRequestDto dto) {
        
        // 아이디 중복 체크중복
        if (hostMapper.existsByUserId(dto.getUserId())) {
        	throw new BusinessException("이미 사용 중인 아이디입니다.");
        }
        
        // BCrypt 암호화 (Salt를 내부에서 무작위로 생성함)
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        
        Host host = Host.builder()
                .userId(dto.getUserId())
                .password(encodedPassword) // 암호화된 비번 주입
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();
        
        try {
        	hostMapper.insertHost(host);
        } catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
        
    }
    
    @Override
    public Host login(HostSigninRequestDto dto) {
        Host host = hostMapper.findByUserId(dto.getUserId());
        // 존재하지 않거나 비밀번호 불일치 시 예외 발생
        if (host == null || !passwordEncoder.matches(dto.getPassword(), host.getPassword())) {
            throw new BusinessException("아이디 또는 비밀번호가 일치하지 않습니다."); // IllegalArgumentException 핸들러가 처리함
        }
        
        return host;
    }
    
    @Override
    public HostIdDuplicateCheckResponseDto isDuplicateId(String userId) {
    		return HostIdDuplicateCheckResponseDto.builder().available(!hostMapper.existsByUserId(userId)).build();
    }
}