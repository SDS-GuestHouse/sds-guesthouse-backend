package com.sds_guesthouse.model.dto.host;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.sds_guesthouse.model.entity.Host;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostIdDuplicateCheckResponseDto {
    private boolean available;
}