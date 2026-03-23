package com.sds_guesthouse.model.dto.guest;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.sds_guesthouse.util.validation.ValidPassword;
import com.sds_guesthouse.util.validation.ValidUserId;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestSigninRequestDto {
	
	@ValidUserId
    private String userId;
    
	@ValidPassword
	private String password;
}