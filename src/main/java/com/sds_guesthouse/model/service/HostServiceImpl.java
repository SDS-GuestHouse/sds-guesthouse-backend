package com.sds_guesthouse.model.service;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dto.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;
import com.sds_guesthouse.model.mapper.HostMapper;
import com.sds_guesthouse.util.OpenCrypt;

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
        
        // 1. 아이디 중복 체크 (보안 강화 영역)
        // 중복 시 IllegalArgumentException을 던져서 "입력 정보가 올바르지 않습니다"로 응답 통일
        if (hostMapper.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("Duplicate ID: " + dto.getUserId());
        }
        
        // 2. BCrypt 암호화 (Salt를 따로 넣지 않아도 내부에서 무작위로 생성함)
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        
        // 이전버전 : OpenCrypt 실습 파일 비밀번호 암호화 (SHA-256 + Salt)
        // userId를 이용해 salt를 만드는 방식은 userId가 탈취당하면 소용 없음..?
        // Salt는 보통 유저의 ID나 생성일자 등을 섞어서 암호문의 예측을 어렵게 만듭니다.
        // byte[] encryptedPassword = OpenCrypt.getSHA256(dto.getPassword(), dto.getUserId());
        // String hexPassword = OpenCrypt.byteArrayToHex(encryptedPassword);
        
        // 3. DTO -> Entity 변환 (빌더 패턴 활용)
        Host host = Host.builder()
                .userId(dto.getUserId())
                .password(encodedPassword) // 암호화된 비번 주입
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();

        // 4. DB 저장 호출
        int result = hostMapper.insertHost(host);
        
        if (result == 0) {
            throw new ExplicitMessageException("회원가입 중 서버 오류가 발생했습니다.");
        }
    }
}