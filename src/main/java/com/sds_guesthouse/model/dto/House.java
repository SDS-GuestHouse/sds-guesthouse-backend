package com.sds_guesthouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {
    private Integer houseId;
    private Integer hostId;
    private String name;
    private Integer price;
    private HouseStatus status;
    private String address;
    private Integer maxGuests;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}