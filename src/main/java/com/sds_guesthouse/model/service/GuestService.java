package com.sds_guesthouse.model.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sds_guesthouse.model.dao.GuestDao;
import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.entity.Guest;
import com.sds_guesthouse.util.auth.SessionUser;


@Service
public class GuestService {
    private final GuestDao guestDao;
    private final PasswordEncoder passwordEncoder;

    public GuestService(GuestDao guestDao, PasswordEncoder passwordEncoder) {
        this.guestDao = guestDao;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    Signup
    */
    public void signUp(GuestSignupRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            guestDao.insertGuest(dto);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 사용 중인 아이디입니다."
            );
        }
    }

    /*
    Signin
    */    
    public SessionUser signInGuest(GuestSigninRequestDto dto) {
        String userId = dto.getUserId();
        String rawPassword = dto.getPassword();
        Guest guest = guestDao.getGuestByUserId(userId);

        if (guest == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "아이디 또는 비밀번호가 올바르지 않습니다."
            );
        }

        if (!passwordEncoder.matches(rawPassword, guest.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "아이디 또는 비밀번호가 올바르지 않습니다."
            );
        }

        return SessionUser.fromGuest(guest);
    }


    /*
    Check
    */
    public GuestCheckResponseDto checkUserId(GuestCheckRequestDto dto) {
        boolean available = guestDao.countByUserId(dto.getUserId()) == 0;
        return new GuestCheckResponseDto(available);
    }
}