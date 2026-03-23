package com.sds_guesthouse.model.dto.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostIdDuplicateCheckResponseDto {
    private boolean available;
}