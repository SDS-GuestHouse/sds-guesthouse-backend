package com.sds_guesthouse.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HouseCreateRequestDto {

    @NotBlank(message = "숙소 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;

    private String description;
    
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    private int maxGuests;
}