package com.sds_guesthouse.controller;

import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping("/check")
    public ResponseEntity<GuestCheckResponseDto> checkUserId(
            @RequestBody GuestCheckRequestDto dto
    ) {
        GuestCheckResponseDto response = guestService.checkUserId(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(
            @RequestBody GuestSignupRequestDto dto
    ) {
        boolean result = guestService.signUp(dto);

        if (result) {
            return ResponseEntity.ok("signup success");
        }

        return ResponseEntity.badRequest().body("signup fail");
    }
}