package com.sds_guesthouse.model.dto.admin;

import com.sds_guesthouse.model.entity.HouseStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminHouseManageRequestDto {
	
	@NotNull(message = "조회할 상태값(status)은 필수입니다.")
    private HouseStatus status; // CREATE_PENDING 또는 DELETE_PENDING
}