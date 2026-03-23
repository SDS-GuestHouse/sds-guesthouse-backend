package com.sds_guesthouse.model.service;


import org.springframework.stereotype.Service;
import com.sds_guesthouse.model.dao.ReservationMapper;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Override
    public List<Reservation> getReservations(ReservationStatus status, String date) {
        return reservationMapper.findReservations(status, date);
    }

    @Override
    public Reservation getReservationByReservationId(Long reservationId) {
    	return reservationMapper.findReservationByReservationId(reservationId);
    }

}