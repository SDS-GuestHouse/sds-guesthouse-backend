package com.sds_guesthouse.model.dao;

import com.sds_guesthouse.model.dto.guest.GuestSignupRequestDto;
import com.sds_guesthouse.model.dto.guest.GuestSigninRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds_guesthouse.model.entity.Guest;

@Mapper
public interface GuestDao {
    // 아이디 중복 체크
    int countByUserId(@Param("userId") String userId);

    // 회원가입
    int insertGuest(GuestSignupRequestDto dto);

    // 로그인
    Guest getGuestByUserId(@Param("userId") String userId);
}