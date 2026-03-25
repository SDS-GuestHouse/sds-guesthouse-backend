package com.sds_guesthouse.model.dto.house;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class HouseRequestDtoValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void price_allowsUpperBoundary() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", 1_000_000_000L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void price_rejectsValuesAboveUpperBoundary() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", 1_000_000_001L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }
}
