package com.sds_guesthouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sds_guesthouse.model.dto.HouseCreateRequestDto;
import com.sds_guesthouse.model.service.HouseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    /**
     * 숙소 등록 요청 API
     * POST /api/v1/houses
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createHouse(
            @Valid @RequestBody HouseCreateRequestDto dto) {

        houseService.createHouse(dto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "숙소 등록 요청이 완료되었습니다.");

        return ResponseEntity.ok(response);
    }
}