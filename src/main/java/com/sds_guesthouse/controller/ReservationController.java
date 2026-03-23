package com.sds_guesthouse.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import com.sds_guesthouse.model.service.ReservationService;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;


import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) String date
    ) {

        List<Reservation> reservations = reservationService.getReservations(status, date);

        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationByReservationId(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservationByReservationId(reservationId);
        
        return ResponseEntity.ok(reservation);
    }
}
