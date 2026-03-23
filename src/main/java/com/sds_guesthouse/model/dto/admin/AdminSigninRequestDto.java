package com.sds_guesthouse.model.dto.admin;

import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 관리자 로그인 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // 로그 찍을 때 비밀번호는 가려주기
public class AdminSigninRequestDto {
	
    private String userId;   // 로그인 시도 아이디
    private String password; // 로그인 시도 비밀번호 (평문)

}
