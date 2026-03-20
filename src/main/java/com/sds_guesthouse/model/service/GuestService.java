package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dao.GuestDao;
import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninResponseDto;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestService {

    private final GuestDao guestDao;
    private final PasswordEncoder passwordEncoder;


    public GuestService(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    public boolean signUp(GuestSignupRequestDto dto) {
        if (guestDao.countByUserId(dto.getUserId()) > 0) {
            return false;
        }
        return guestDao.insertGuest(dto) == 1;
    }

    public GuestSigninResponseDto signIn(GuestSigninRequestDto dto) {
        string userId = dto.getUserId();
        string password = dto.getPassword();

        string hashed = passwordEncoder.encode(dto.getPassword())

        int count = guestDao.countByUserIdAndPassword(userId, hashed);
        
        if (count < 1) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        else if(count > 1){
            System.err.println("why it happened??");
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return host;
    }

    public GuestCheckResponseDto checkUserId(GuestCheckRequestDto dto) {
        boolean available = guestDao.countByUserId(dto.getUserId()) == 0;
        return new GuestCheckResponseDto(available);
    }
}