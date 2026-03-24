package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface HouseService {

    /**
     * 숙소 등록 요청
     */
    void createHouse(HouseRequestDto dto);
    House getHouseDetail(Long houseId);
    void updateHouse(Long houseId, HouseRequestDto dto);
    void deleteHouse(Long houseId);
    List<Reservation> getReservationsByHouseId(Long houseId);
    List<House> getAvailableHouses(LocalDate startDate, LocalDate endDate, String location, Integer numberOfGuests);
}