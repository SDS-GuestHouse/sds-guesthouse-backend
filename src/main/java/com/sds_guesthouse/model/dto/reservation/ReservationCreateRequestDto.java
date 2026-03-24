package com.sds_guesthouse.model.dto.reservation;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateRequestDto {

    @NotNull(message = "checkinDate is required")
    private LocalDate checkinDate;

    @NotNull(message = "checkoutDate is required")
    private LocalDate checkoutDate;

    @AssertTrue(message = "checkoutDate must be after checkinDate")
    public boolean isStayRangeValid() {
        if (checkinDate == null || checkoutDate == null) {
            return true;
        }
        return checkoutDate.isAfter(checkinDate);
    }
}
