package com.sds_guesthouse.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseImage {

    private Long imageId;    // 이미지 고유 ID
    private Long houseId;    // 해당 이미지가 속한 숙소 ID
    public String imagePath;  // 이미지 파일 경로 (UUID로 저장된 경로)
    private int display_order;       // 이미지 순서
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}