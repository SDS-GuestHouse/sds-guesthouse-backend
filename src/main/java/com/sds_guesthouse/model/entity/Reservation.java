package com.sds_guesthouse.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Reservation {
    private Long reservationId;     // 예약 고유 ID
    private Long guestId;           // 예약한 게스트 ID (FK)
    private Long houseId;           // 예약 대상 숙소 ID (FK)
    private ReservationStatus status;  // 예약 상태 (pending, confirmed, cancelled, completed)
    private LocalDate checkinDate;  // 체크인 날짜
    private LocalDate checkoutDate; // 체크아웃 날짜
    private int totalPrice;         // 예약 총 금액
    private LocalDateTime createdAt;  // 생성일
    private LocalDateTime updatedAt;  // 수정일
}