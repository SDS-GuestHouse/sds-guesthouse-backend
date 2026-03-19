package com.sds_guesthouse.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // 로그 출력 시 비밀번호 보안 유지
public class Host {

    private Long hostId;        // PK (자동 증가분)
    private String userId;      // 호스트 아이디
    private String password;    // 암호화된 비밀번호 (SHA-256)
    private String name;        // 호스트 이름
    private String phone;       // 연락처
    private LocalDateTime createdAt; // 가입 일시
    private LocalDateTime updatedAt; // 수정 일시
    
}