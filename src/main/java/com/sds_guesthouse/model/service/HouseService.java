package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;

public interface HouseService {

    /**
     * 숙소 등록 요청
     */
    void createHouse(HouseRequestDto dto);
    House getHouseDetail(Long houseId);
    void updateHouse(Long houseId, HouseRequestDto dto);
}