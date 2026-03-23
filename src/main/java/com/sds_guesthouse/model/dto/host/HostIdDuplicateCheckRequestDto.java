package com.sds_guesthouse.model.dto.host;

import com.sds_guesthouse.util.validation.ValidUserId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 호스트 아이디 중복 확인 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HostIdDuplicateCheckRequestDto {
	@ValidUserId
    private String userId;
}