package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

import java.util.List;

public interface ReservationService {
    List<Reservation> getReservations(ReservationStatus status, String date);
    Reservation getReservationByReservationId(Long reservationId);
    String cancelReservation(Long reservationId);
}