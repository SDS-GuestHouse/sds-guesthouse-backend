package com.sds_guesthouse.model.dto.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 호스트 로그인 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSigninRequestDto {

    private String userId;   // 로그인 시도 아이디
    private String password; // 로그인 시도 비밀번호 (평문)

}