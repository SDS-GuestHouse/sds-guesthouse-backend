package com.sds_guesthouse.model.dto.house;

import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseCreateResponseDto {
	private Long houseId;

}
