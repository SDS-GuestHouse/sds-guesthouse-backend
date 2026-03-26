package com.sds_guesthouse.model.dto.house;

import java.util.List;

import com.sds_guesthouse.model.entity.House;

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
