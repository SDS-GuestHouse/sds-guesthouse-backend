package com.sds_guesthouse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class GuestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 성공 후 같은 아이디 체크 시 available=false")
    void signup_then_check() throws Exception {
        mockMvc.perform(post("/guest/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "integrationUser",
                              "password": "1234",
                              "name": "kim",
                              "phone": "010-1234-5678"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("signup success"));

        mockMvc.perform(post("/guest/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "integrationUser"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
}