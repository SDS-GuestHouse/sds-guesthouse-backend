package com.sds_guesthouse.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.dto.reservation.ReservationCreateRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.service.HouseService;
import com.sds_guesthouse.model.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createHouse(@Valid @RequestBody HouseRequestDto dto) {
        houseService.createHouse(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<House> getHouseDetail(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseService.getHouseDetail(houseId));
    }

    @GetMapping("/my-house")
    public ResponseEntity<List<House>> getMyHouses() {
        return ResponseEntity.ok(houseService.getMyHouses());
    }

    @PutMapping("/{houseId}")
    public ResponseEntity<Void> updateHouse(
            @PathVariable Long houseId,
            @Valid @RequestBody HouseRequestDto dto
    ) {
        houseService.updateHouse(houseId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{houseId}")
    public ResponseEntity<Map<String, String>> deleteHouse(@PathVariable Long houseId) {
        houseService.deleteHouse(houseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "House deletion request has been submitted.");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/{houseId}/reservation")
    public ResponseEntity<List<Reservation>> getReservationsByHouseId(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseService.getReservationsByHouseId(houseId));
    }

    @PostMapping("/{houseId}/reserve")
    public ResponseEntity<Void> reserveHouse(
            @PathVariable Long houseId,
            @Valid @RequestBody ReservationCreateRequestDto dto
    ) {
        reservationService.reserveHouse(houseId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<House>> getAvailableHouses(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer numberOfGuests
    ) {
        return ResponseEntity.ok(houseService.getAvailableHouses(startDate, endDate, location, numberOfGuests));
    }

    @PostMapping(
            value = "/{houseId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadHouseImage(
            @PathVariable Long houseId,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        houseService.uploadHouseImage(houseId, imageFile);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Image uploaded successfully.");
        return ResponseEntity.ok(response);
    }
}
