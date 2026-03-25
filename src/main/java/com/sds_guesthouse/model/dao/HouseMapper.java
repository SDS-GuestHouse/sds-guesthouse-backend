package com.sds_guesthouse.model.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;

@Mapper
public interface HouseMapper {

    int insertHouse(House house);

    House findById(Long houseId);

    List<House> findByHostId(@Param("hostId") Long hostId);

    int updateHouse(House house);

    List<Reservation> findReservationsByHouseId(Long houseId);

    List<House> findByStatus(HouseStatus status);

    int updateStatus(long houseId, HouseStatus status);

    long countAvailableHouses(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("location") String location,
            @Param("numberOfGuests") Integer numberOfGuests
    );

    List<House> findAvailableHouses(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("location") String location,
            @Param("numberOfGuests") Integer numberOfGuests,
            @Param("limit") int limit,
            @Param("offset") long offset
    );
}