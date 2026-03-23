package com.sds_guesthouse.model.dto.host;

import com.sds_guesthouse.util.validation.ValidPassword;
import com.sds_guesthouse.util.validation.ValidUserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 호스트 로그인 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HostSigninRequestDto {
	
	@ValidUserId
    private String userId;   // 로그인 시도 아이디
    
	@ValidPassword
	private String password; // 로그인 시도 비밀번호 (평문)

}