package com.sds_guesthouse.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sds_guesthouse.model.dao.HostMapper;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	@Override
	public Admin login(HostSigninRequestDto dto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
