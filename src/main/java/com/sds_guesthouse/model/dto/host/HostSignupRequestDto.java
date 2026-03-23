package com.sds_guesthouse.model.dto.host;

import com.sds_guesthouse.util.validation.ValidName;
import com.sds_guesthouse.util.validation.ValidPassword;
import com.sds_guesthouse.util.validation.ValidPhone;
import com.sds_guesthouse.util.validation.ValidUserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // 로그 찍을 때 비밀번호는 가려주기
public class HostSignupRequestDto {
	
	@ValidUserId
    private String userId;

    @ValidName
    private String name;

    @ValidPassword
    private String password;

    @ValidPhone
    private String phone;
    
}
