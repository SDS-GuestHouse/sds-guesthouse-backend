package com.sds_guesthouse.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.sds_guesthouse.model.entity.Admin;

@Mapper // MyBatis 매퍼임을 명시합니다.
public interface AdminMapper {
	
    /**
     * 아이디로 호스트 정보 조회 (로그인용)
     * @param userId 조회할 아이디
     * @return 조회된 관리자 엔티티
     */
    Admin findByUserId(String userId);

}
