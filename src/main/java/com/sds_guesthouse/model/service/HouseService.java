package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.house.HouseCreateResponseDto;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface HouseService {

    /**
     * 숙소 등록 요청
     * @param hostId 
     * @return 
     */
    HouseCreateResponseDto createHouse(Long hostId, HouseRequestDto dto);
    House getHouseDetail(Long houseId);
    void updateHouse(Long hostId, Long houseId, HouseRequestDto dto);
    void deleteHouse(Long houseId);
    List<Reservation> getReservationsByHouseId(Long houseId);
    List<House> getAvailableHouses(LocalDate startDate, LocalDate endDate, String location, Integer numberOfGuests);
}