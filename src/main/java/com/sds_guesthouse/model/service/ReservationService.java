package com.sds_guesthouse.model.service;

import java.util.List;

import com.sds_guesthouse.model.dto.reservation.ReservationCreateRequestDto;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

public interface ReservationService {

    void reserveHouse(Long houseId, ReservationCreateRequestDto dto);

    List<Reservation> getReservations(ReservationStatus status, String date);

    Reservation getReservationByReservationId(Long reservationId);

    String cancelReservation(Long reservationId);
}
