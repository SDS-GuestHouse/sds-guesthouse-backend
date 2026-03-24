package com.sds_guesthouse.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sds_guesthouse.model.dto.house.HouseCreateResponseDto;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.service.HouseImageService;
import com.sds_guesthouse.model.service.HouseService;
import com.sds_guesthouse.util.auth.SessionUser;

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
    public ResponseEntity<HouseCreateResponseDto> createHouse(
            @Valid @RequestBody HouseRequestDto dto,
            @AuthenticationPrincipal SessionUser sessionUser) {
    	HouseCreateResponseDto response = houseService.createHouse(sessionUser.getId(), dto);
        return ResponseEntity.ok(response);
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
        @Valid @RequestBody HouseRequestDto dto,
        @AuthenticationPrincipal SessionUser sessionUser) {
	    houseService.updateHouse(sessionUser.getId(), houseId, dto);
	    return ResponseEntity.ok().build();
    }
    
    /**
     * 숙소 삭제 요청
     * /api/v1/house/{id}
     */
    @DeleteMapping("/{houseId}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long houseId) {
        houseService.deleteHouse(houseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "숙소 삭제 요청이 완료되었습니다. 관리자 승인 대기 중입니다.");

        return ResponseEntity.ok().build();
    }
    
    /**
     * 각 숙소별 예약 정보
     * /api/v1/house/{id}/reservation
     */
    @GetMapping("/{houseId}/reservation")
    public ResponseEntity<List<Reservation>> getReservationsByHouseId(@PathVariable Long houseId) {
        List<Reservation> reservations = houseService.getReservationsByHouseId(houseId);
        
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * 예약 가능 숙소 목록
     * /api/v1/house
     */
    @GetMapping
    public ResponseEntity<List<House>> getAvailableHouses(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer numberOfGuests) {

        List<House> availableHouses = houseService.getAvailableHouses(startDate, endDate, location, numberOfGuests);

        return ResponseEntity.ok(availableHouses);
    }
    
    /**
     * 이미지 업로드
     * /api/v1/house/{houseId}/image
     */
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