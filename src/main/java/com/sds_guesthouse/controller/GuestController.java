package com.sds_guesthouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.service.GuestService;
import com.sds_guesthouse.util.auth.SecurityLoginService;
import com.sds_guesthouse.util.auth.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/v1/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    private final SecurityLoginService securityLoginService;

    /*
    Signup
    */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody GuestSignupRequestDto dto
    ) {
        guestService.signUp(dto);
        return ResponseEntity.ok().build();
    }

    /*
    Signin
    */
    @PostMapping("/signin")
    public ResponseEntity<Void> signIn(
            @RequestBody GuestSigninRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SessionUser sessionUser = guestService.signInGuest(dto);
        securityLoginService.login(sessionUser, request, response);
        return ResponseEntity.ok().build();
    }

    /*
    Check
    */
    @PostMapping("/check")
    public ResponseEntity<GuestCheckResponseDto> checkUserId(
        @RequestBody GuestCheckRequestDto dto
    ) {
        GuestCheckResponseDto response = guestService.checkUserId(dto);
        return ResponseEntity.ok(response);
    }

}