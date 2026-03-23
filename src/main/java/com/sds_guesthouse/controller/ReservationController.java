package com.sds_guesthouse.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import com.sds_guesthouse.model.service.ReservationService;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable Long reservationId) {
        String result = reservationService.cancelReservation(reservationId);
    	Map<String, String> response = new HashMap<>();
        switch (result) {
	        case "NOT_FOUND":
	            response.put("message", "예약을 찾을 수 없습니다.");
	            break;
	        case "ALREADY_CANCELLED":
	            response.put("message", "예약이 이미 취소된 상태입니다.");
	            break;
	        case "SUCCESS":
	            response.put("message", "예약이 성공적으로 취소되었습니다.");
	            break;
	        default:
	            response.put("message", "알 수 없는 오류 발생");
       }
        return ResponseEntity.ok(response);
    }
}
