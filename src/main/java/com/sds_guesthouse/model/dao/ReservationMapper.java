package com.sds_guesthouse.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

import java.util.List;

@Mapper
public interface ReservationMapper {

	List<Reservation> findReservations(
	    @Param("status") ReservationStatus status,
	    @Param("date") String date
	);
}