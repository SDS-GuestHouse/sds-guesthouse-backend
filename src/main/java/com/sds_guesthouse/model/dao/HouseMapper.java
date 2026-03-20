package com.sds_guesthouse.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.sds_guesthouse.model.entity.House;

@Mapper
public interface HouseMapper {

    /**
     * 숙소 등록
     * @param house 숙소 엔티티
     * @return 영향받은 행 수
     */
    int insertHouse(House house);
}