package com.sds_guesthouse.model.dao;

import java.util.List;
import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;

@Mapper
public interface HouseMapper {

    /**
     * 숙소 등록
     * @param house 숙소 엔티티
     * @return 영향받은 행 수
     */
    int insertHouse(House house);
    House findById(Long houseId);
    int updateHouse(House house);
	List<Reservation> findReservationsByHouseId(Long houseId);
	List<House> findByStatus(HouseStatus status);
	int updateStatus(long houseId, HouseStatus status);
	List<House> findAvailableHouses(LocalDate startDate, LocalDate endDate, String location, Integer numberOfGuests);
	List<House> findMyHouses(Long hostId);
}