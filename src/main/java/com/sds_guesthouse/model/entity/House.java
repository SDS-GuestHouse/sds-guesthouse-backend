package com.sds_guesthouse.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class House {

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
}
