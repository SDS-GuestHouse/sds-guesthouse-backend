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
    void fields_allowConfiguredUpperBoundaries() {
        HouseRequestDto dto = new HouseRequestDto(
                "n".repeat(50),
                "a".repeat(50),
                1_000_000_000L,
                "d".repeat(1000),
                3
        );

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void price_allowsLowerBoundary() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", 0L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void price_rejectsValuesBelowLowerBoundary() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", -1L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertSingleViolation(violations, "price");
    }

    @Test
    void price_rejectsValuesAboveUpperBoundary() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", 1_000_000_001L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertSingleViolation(violations, "price");
    }

    @Test
    void name_rejectsValuesAbove50Characters() {
        HouseRequestDto dto = new HouseRequestDto("n".repeat(51), "Seoul", 120000L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertSingleViolation(violations, "name");
    }

    @Test
    void address_rejectsValuesAbove50Characters() {
        HouseRequestDto dto = new HouseRequestDto("House A", "a".repeat(51), 120000L, "desc", 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertSingleViolation(violations, "address");
    }

    @Test
    void description_rejectsValuesAbove1000Characters() {
        HouseRequestDto dto = new HouseRequestDto("House A", "Seoul", 120000L, "d".repeat(1001), 3);

        Set<ConstraintViolation<HouseRequestDto>> violations = validator.validate(dto);

        assertSingleViolation(violations, "description");
    }

    private void assertSingleViolation(Set<ConstraintViolation<HouseRequestDto>> violations, String propertyName) {
        assertEquals(1, violations.size());
        assertEquals(propertyName, violations.iterator().next().getPropertyPath().toString());
    }
}
