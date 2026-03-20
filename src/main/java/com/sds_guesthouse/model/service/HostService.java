package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.HostSignupRequestDto;

public interface HostService {
    /**
     * 호스트 회원가입 로직
     * @param dto 가입 신청 정보
     * @throws IllegalArgumentException 보안상 상세 사유를 숨겨야 할 때
     * @throws ExplicitMessageException 사용자에게 상세 사유를 전달해야 할 때
     */
    void registerHost(HostSignupRequestDto dto);
}