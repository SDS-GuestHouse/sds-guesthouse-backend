package com.sds_guesthouse.model.dto.house;

import java.time.LocalDateTime;
import java.util.List;

import com.sds_guesthouse.model.entity.CheckInOut;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HouseResponseDto {
	
    private Long houseId;
    private Long hostId;
    private String name;
    private String address;
    private long price;
    private int maxGuests;
    private String description;
    private HouseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<CheckInOut> checkInOutList;
    
    public static HouseResponseDto fromHouse(House house) {
    	return HouseResponseDto.builder()
    			.houseId(house.getHouseId())
    			.hostId(house.getHostId())
    			.name(house.getName())
    			.address(house.getAddress())
    			.price(house.getPrice())
    			.maxGuests(house.getMaxGuests())
    			.description(house.getDescription())
    			.status(house.getStatus())
    			.createdAt(house.getCreatedAt())
    			.updatedAt(house.getUpdatedAt()).build();
    }

}
