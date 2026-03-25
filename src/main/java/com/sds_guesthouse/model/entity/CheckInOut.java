package com.sds_guesthouse.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CheckInOut {

    private LocalDate checkinDate;  // 체크인 날짜
    private LocalDate checkoutDate; // 체크아웃 날짜
}
