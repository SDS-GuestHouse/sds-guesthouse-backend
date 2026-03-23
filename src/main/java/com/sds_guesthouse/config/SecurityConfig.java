package com.sds_guesthouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {
	
	@Bean
    public SecurityContextRepository securityContextRepository() {
        // 세션을 사용하여 보안 컨텍스트를 유지하는 가장 일반적인 방식입니다.
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	// 1. CSRF 보안 비활성화 (테스트 편의를 위해, 실무에선 설정 필요)
        	.csrf(csrf -> csrf.disable())
        	
        	// 2. URL별 권한 제어
            // .authorizeHttpRequests(auth -> auth
            //     // 호스트 전용 API는 ROLE_HOST 권한이 있어야만 접근 가능
            //     .requestMatchers("/api/v1/host/**").hasRole("HOST")
            //     // 로그인, 회원가입 등 공통 API는 누구나 접근 가능
            //     .requestMatchers("/api/v1/auth/**", "/signin", "/signup").permitAll()
            //     // 그 외 모든 요청은 인증(로그인)이 필요함
            //     .anyRequest().authenticated()
            // )
            
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 모든 요청을 인증 없이 허용 (로그인 상태 아니어도 인증되도록)
            )
            
         // 3. 세션 관리 전략
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 없으면 새로 생성하고, 있으면 기존 것 사용 (IF_REQUIRED)
            );
        return http.build();

    }


}	