package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dao.GuestDao;
import com.sds_guesthouse.model.dto.guest.GuestCheckRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    private final GuestDao guestDao;

    public GuestService(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    public GuestCheckResponseDto checkUserId(GuestCheckRequestDto dto) {
        boolean available = guestDao.countByUserId(dto.getUserId()) == 0;
        return new GuestCheckResponseDto(available);
    }

    public boolean signUp(GuestSignupRequestDto dto) {
        if (guestDao.countByUserId(dto.getUserId()) > 0) {
            return false;
        }
        return guestDao.insertGuest(dto) == 1;
    }
}