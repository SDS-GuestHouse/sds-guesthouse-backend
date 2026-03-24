package com.sds_guesthouse.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sds_guesthouse.model.service.HouseService;
import com.sds_guesthouse.model.service.HouseImageService;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.House;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final HouseImageService houseImageService;
    /**
     * 숙소 등록 요청 API
     * POST /api/v1/houses
     */
    @PostMapping
    public ResponseEntity<Void> createHouse(
            @Valid @RequestBody HouseRequestDto dto) {
        houseService.createHouse(dto);
        Map<String, String> response = new HashMap<>();
        return ResponseEntity.ok().build();
    }
    
    /**
     * 숙소 상세조회
     * GET /api/v1/house/{houseId}
     */
    @GetMapping("/{houseId}")
    public ResponseEntity<House> getHouseDetail(@PathVariable Long houseId) {
        House house = houseService.getHouseDetail(houseId);
        return ResponseEntity.ok(house);
    }
    
    
    /**
     * 숙소 정보 수정
     * GET /api/v1/house/{houseId}
     */
    @PutMapping("/{houseId}")
    public ResponseEntity<Void> updateHouse(
        @PathVariable Long houseId, 
        @Valid @RequestBody HouseRequestDto dto) {
	    houseService.updateHouse(houseId, dto);
	    return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{houseId}")
    public ResponseEntity<Map<String, String>> deleteHouse(@PathVariable Long houseId) {
        houseService.deleteHouse(houseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "숙소 삭제 요청이 완료되었습니다. 관리자 승인 대기 중입니다.");

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{houseId}/reservation")
    public ResponseEntity<List<Reservation>> getReservationsByHouseId(@PathVariable Long houseId) {
        List<Reservation> reservations = houseService.getReservationsByHouseId(houseId);
        
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping
    public ResponseEntity<List<House>> getAvailableHouses(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer numberOfGuests) {

        List<House> availableHouses = houseService.getAvailableHouses(startDate, endDate, location, numberOfGuests);

        return ResponseEntity.ok(availableHouses);
    }
    
    @PostMapping("/{houseId}/image")
    public ResponseEntity<Map<String, String>> uploadHouseImage(
            @PathVariable Long houseId, 
            @RequestParam("imageFile") MultipartFile imageFile) {

        Map<String, String> response = new HashMap<>();
        try {
            houseImageService.uploadHouseImage(houseId, imageFile);
            response.put("message", "이미지 업로드 성공");
        } catch (Exception e) {
            response.put("message", "이미지 업로드 실패");
        }
        return ResponseEntity.ok(response);

    }
    
    
}