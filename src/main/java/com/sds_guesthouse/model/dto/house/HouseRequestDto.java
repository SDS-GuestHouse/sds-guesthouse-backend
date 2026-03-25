package com.sds_guesthouse.model.dto.house;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class HouseRequestDto {

    @NotBlank(message = "숙소 이름은 필수입니다.")
    @Size(max = 50, message = "name must be less than or equal to 50 characters")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 50, message = "address must be less than or equal to 50 characters")
    private String address;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    @Max(value = 1_000_000_000L, message = "price must be less than or equal to 1000000000")
    private Long price;

    @Size(max = 1000, message = "description must be less than or equal to 1000 characters")
    private String description;

    @NotNull(message = "인원수는 필수 항목입니다.")
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    private Integer maxGuests;
}
