package com.sds_guesthouse.controller;

import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.service.GuestService;
import lombok.RequiredArgsConstructor;

import com.sds_guesthouse.util.auth.SessionUser;
import com.sds_guesthouse.util.auth.SessionConst;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("api/v1/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

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
        HttpServletRequest request
    ){
        SessionUser sessionUser = guestService.signInGuest(dto);
    
        if(sessionUser == null){
            return ResponseEntity.badRequest().build();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_USER, sessionUser);

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