package com.sds_guesthouse.model.dto.guest;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.sds_guesthouse.util.validation.ValidUserId;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestCheckRequestDto {
	@ValidUserId
    private String userId;
}