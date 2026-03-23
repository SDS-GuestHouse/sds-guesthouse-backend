package com.sds_guesthouse.model.dto.admin;

import com.sds_guesthouse.model.entity.HouseStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminHouseManageRequestDto {
	
	@NotNull(message = "status값은 필수입니다.")
    private HouseStatus status;
}