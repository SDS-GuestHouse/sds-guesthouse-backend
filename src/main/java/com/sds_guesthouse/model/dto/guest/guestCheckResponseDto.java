package com.sds_guesthouse.model.dto.guest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestCheckResponseDto {
    private boolean available;
}