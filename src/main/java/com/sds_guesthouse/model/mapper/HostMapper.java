package com.sds_guesthouse.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sds_guesthouse.model.entity.Host;

@Mapper // MyBatis 매퍼임을 명시합니다.
public interface HostMapper {

    /**
     * 아이디 중복 체크
     * @param userId 중복 여부를 확인할 아이디
     * @return 존재하면 true, 없으면 false (MyBatis가 1/0을 boolean으로 자동 매핑)
     */
    boolean existsByUserId(String userId);

    /**
     * 호스트 정보 저장
     * @param host 저장할 호스트 엔티티 객체
     * @return 영향받은 행의 수 (성공 시 1)
     */
    int insertHost(Host host);
}