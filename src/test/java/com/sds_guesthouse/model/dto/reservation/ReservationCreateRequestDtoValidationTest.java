package com.sds_guesthouse.model.dto.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ReservationCreateRequestDtoValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void stayLength_allows365Nights() {
        ReservationCreateRequestDto dto = new ReservationCreateRequestDto(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1)
        );

        Set<ConstraintViolation<ReservationCreateRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void stayLength_rejects366Nights() {
        ReservationCreateRequestDto dto = new ReservationCreateRequestDto(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 2)
        );

        Set<ConstraintViolation<ReservationCreateRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("stayLengthValid", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void stayLength_rejectsNonPositiveStay() {
        ReservationCreateRequestDto dto = new ReservationCreateRequestDto(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1)
        );

        Set<ConstraintViolation<ReservationCreateRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("stayRangeValid", violations.iterator().next().getPropertyPath().toString());
    }
}
