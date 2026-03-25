package com.sds_guesthouse.model.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sds_guesthouse.model.dto.house.HouseListResponseDto;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.Reservation;

public interface HouseService {

    void createHouse(HouseRequestDto dto);

    House getHouseDetail(Long houseId);

    List<String> getHouseImagePaths(Long houseId);

    List<House> getMyHouses();

    void updateHouse(Long houseId, HouseRequestDto dto);

    void deleteHouse(Long houseId);

    List<Reservation> getReservationsByHouseId(Long houseId);

    HouseListResponseDto getAvailableHouses(
            LocalDate startDate,
            LocalDate endDate,
            String location,
            Integer numberOfGuests,
            Integer page
    );

    void uploadHouseImage(Long houseId, MultipartFile imageFile);
}