package com.sds_guesthouse.model.dao;

import com.sds_guesthouse.model.entity.HouseImage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseImageMapper {

    // 해당 houseId에 대한 최대 order 값 조회
    Integer findMaxOrderByHouseId(@Param("houseId") Long houseId);

    // 이미지 업로드 및 DB 저장
    void insertHouseImage(HouseImage houseImage);

    // 이미지 order 업데이트 (필요할 경우)
    void updateHouseImageOrder(@Param("houseId") Long houseId);
}