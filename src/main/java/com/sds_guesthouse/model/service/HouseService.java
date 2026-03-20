package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.HouseCreateRequestDto;
import com.sds_guesthouse.model.entity.House;

public interface HouseService {

    /**
     * 숙소 등록 요청
     */
    void createHouse(HouseCreateRequestDto dto);
    House getHouseDetail(Long houseId);
}