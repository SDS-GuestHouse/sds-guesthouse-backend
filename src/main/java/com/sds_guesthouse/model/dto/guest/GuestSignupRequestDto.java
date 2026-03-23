package com.sds_guesthouse.model.dto.guest;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.sds_guesthouse.util.validation.ValidName;
import com.sds_guesthouse.util.validation.ValidPassword;
import com.sds_guesthouse.util.validation.ValidPhone;
import com.sds_guesthouse.util.validation.ValidUserId;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestSignupRequestDto {
	
	@ValidUserId
    private String userId;
    
	@ValidPassword
	private String password;
    
	@ValidName
    private String name;
    
	@ValidPhone
    private String phone;
}