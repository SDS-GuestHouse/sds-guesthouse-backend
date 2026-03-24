package com.sds_guesthouse.model.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

@Mapper
public interface ReservationMapper {

    int insertReservation(Reservation reservation);

    int countOverlappingActiveReservations(
            @Param("houseId") Long houseId,
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate
    );

    List<Reservation> findReservationsByGuestId(
            @Param("guestId") Long guestId,
            @Param("status") ReservationStatus status,
            @Param("date") String date
    );

    Reservation findReservationByReservationIdAndGuestId(
            @Param("reservationId") Long reservationId,
            @Param("guestId") Long guestId
    );

    Reservation findReservationByReservationId(Long reservationId);

    int updateReservationStatus(
            @Param("reservationId") Long reservationId,
            @Param("status") ReservationStatus status
    );
}
