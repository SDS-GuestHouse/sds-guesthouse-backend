package com.sds_guesthouse.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.sds_guesthouse.model.dto.guest.GuestCheckResponseDto;
import com.sds_guesthouse.model.dto.house.HouseListResponseDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;
import com.sds_guesthouse.model.service.AdminService;
import com.sds_guesthouse.model.service.GuestService;
import com.sds_guesthouse.model.service.HostService;
import com.sds_guesthouse.model.service.HouseImageService;
import com.sds_guesthouse.model.service.HouseService;
import com.sds_guesthouse.model.service.ReservationService;
import com.sds_guesthouse.util.auth.SessionUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GuestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuestService guestService;

    @MockitoBean
    private HostService hostService;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private HouseService houseService;

    @MockitoBean
    private HouseImageService houseImageService;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("anonymous users get 401 on protected endpoints")
    void protectedEndpoint_withoutSession_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/reservation"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("public endpoints stay open without a session")
    void publicEndpoint_withoutSession_returnsOk() throws Exception {
        when(guestService.checkUserId(any())).thenReturn(new GuestCheckResponseDto(true));

        mockMvc.perform(post("/api/v1/guest/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "guest100"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("public house image path endpoint returns stored image paths")
    void publicHouseImagePathEndpoint_returnsImagePaths() throws Exception {
        when(houseService.getHouseImagePaths(1L)).thenReturn(List.of("uuid-a.jpg", "uuid-b.jpg"));

        mockMvc.perform(get("/api/v1/house/1/image"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("uuid-a.jpg"))
                .andExpect(jsonPath("$[1]").value("uuid-b.jpg"));

        verify(houseService).getHouseImagePaths(1L);
    }

    @Test
    @DisplayName("public house image path endpoint returns empty array when house has no images")
    void publicHouseImagePathEndpoint_returnsEmptyArray() throws Exception {
        when(houseService.getHouseImagePaths(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/house/2/image"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(houseService).getHouseImagePaths(2L);
    }

    @Test
    @DisplayName("public house list endpoint returns paginated response")
    void publicHouseList_withoutSession_returnsPaginatedResponse() throws Exception {
        when(houseService.getAvailableHouses(null, null, null, null, 2)).thenReturn(
                HouseListResponseDto.builder()
                        .houses(List.of(
                                House.builder()
                                        .houseId(21L)
                                        .hostId(2L)
                                        .name("Page 2 House 1")
                                        .address("Seoul")
                                        .price(150000)
                                        .maxGuests(4)
                                        .description("page two")
                                        .status(HouseStatus.APPROVED)
                                        .build(),
                                House.builder()
                                        .houseId(22L)
                                        .hostId(3L)
                                        .name("Page 2 House 2")
                                        .address("Busan")
                                        .price(170000)
                                        .maxGuests(2)
                                        .description("page two")
                                        .status(HouseStatus.APPROVED)
                                        .build()
                        ))
                        .page(2)
                        .totalPages(3)
                        .totalCount(42L)
                        .build()
        );

        mockMvc.perform(get("/api/v1/house").param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalCount").value(42))
                .andExpect(jsonPath("$.houses[0].houseId").value(21))
                .andExpect(jsonPath("$.houses[0].name").value("Page 2 House 1"))
                .andExpect(jsonPath("$.houses[1].houseId").value(22));

        verify(houseService).getAvailableHouses(null, null, null, null, 2);
    }

    @Test
    @DisplayName("guest session can use guest-only reservation endpoints")
    void guestSession_canUseGuestEndpoints() throws Exception {
        when(guestService.signInGuest(any())).thenReturn(sessionUser(1L, "guest-user", "ROLE_GUEST"));
        when(reservationService.getReservations(null, null)).thenReturn(List.of(
                Reservation.builder()
                        .reservationId(11L)
                        .guestId(1L)
                        .houseId(7L)
                        .status(ReservationStatus.CONFIRMED)
                        .checkinDate(LocalDate.of(2026, 4, 1))
                        .checkoutDate(LocalDate.of(2026, 4, 2))
                        .totalPrice(100000)
                        .build()
        ));

        MockHttpSession session = signInAsGuest();

        mockMvc.perform(get("/api/v1/reservation").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(11));

        verify(reservationService).getReservations(null, null);

        mockMvc.perform(post("/api/v1/house/1/reserve")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "checkinDate": "2026-04-10",
                              "checkoutDate": "2026-04-12"
                            }
                            """))
                .andExpect(status().isOk());

        verify(reservationService).reserveHouse(eq(1L), any());

        mockMvc.perform(get("/api/v1/house/my-house").session(session))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/house/1/reservation").session(session))
                .andExpect(status().isForbidden());

        verifyNoInteractions(houseService);
    }

    @Test
    @DisplayName("host session can use host-only house endpoints")
    void hostSession_canUseHostEndpoints() throws Exception {
        when(hostService.login(any())).thenReturn(sessionUser(2L, "host-user", "ROLE_HOST"));
        when(houseService.getMyHouses()).thenReturn(List.of(
                House.builder()
                        .houseId(2L)
                        .hostId(2L)
                        .name("My House")
                        .address("Seoul")
                        .price(180000)
                        .maxGuests(4)
                        .description("host-owned")
                        .status(HouseStatus.APPROVED)
                        .build()
        ));
        when(houseService.getReservationsByHouseId(1L)).thenReturn(List.of(
                Reservation.builder()
                        .reservationId(21L)
                        .guestId(3L)
                        .houseId(1L)
                        .status(ReservationStatus.PENDING)
                        .checkinDate(LocalDate.of(2026, 5, 1))
                        .checkoutDate(LocalDate.of(2026, 5, 3))
                        .totalPrice(240000)
                        .build()
        ));

        MockHttpSession session = signInAsHost();

        mockMvc.perform(get("/api/v1/house/my-house").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseId").value(2));

        verify(houseService).getMyHouses();

        mockMvc.perform(get("/api/v1/house/1/reservation").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(21));

        verify(houseService).getReservationsByHouseId(1L);

        mockMvc.perform(post("/api/v1/house/1/reserve")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "checkinDate": "2026-05-10",
                              "checkoutDate": "2026-05-12"
                            }
                            """))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/reservation").session(session))
                .andExpect(status().isForbidden());

        verifyNoInteractions(reservationService);
    }

    @Test
    @DisplayName("host house creation accepts valid input")
    void hostHouseCreate_validInput_returnsOk() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        mockMvc.perform(post("/api/v1/house")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseCreateRequest("House A", "Seoul", 120000L, "desc", 3)))
                .andExpect(status().isOk());

        verify(houseService).createHouse(any());
    }

    @Test
    @DisplayName("host house creation rejects names longer than 50 characters")
    void hostHouseCreate_nameLongerThan50_returnsBadRequest() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        expectCreateHouseBadRequest(session, "n".repeat(51), "Seoul", 120000L, "desc", 3);
    }

    @Test
    @DisplayName("host house creation rejects addresses longer than 50 characters")
    void hostHouseCreate_addressLongerThan50_returnsBadRequest() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        expectCreateHouseBadRequest(session, "House A", "a".repeat(51), 120000L, "desc", 3);
    }

    @Test
    @DisplayName("host house creation rejects descriptions longer than 1000 characters")
    void hostHouseCreate_descriptionLongerThan1000_returnsBadRequest() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        expectCreateHouseBadRequest(session, "House A", "Seoul", 120000L, "d".repeat(1001), 3);
    }

    @Test
    @DisplayName("host house creation rejects prices below zero")
    void hostHouseCreate_priceBelowZero_returnsBadRequest() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        expectCreateHouseBadRequest(session, "House A", "Seoul", -1L, "desc", 3);
    }

    @Test
    @DisplayName("host house creation rejects prices above the upper boundary")
    void hostHouseCreate_priceAboveUpperBoundary_returnsBadRequest() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        expectCreateHouseBadRequest(session, "House A", "Seoul", 1_000_000_001L, "desc", 3);
    }

    @Test
    @DisplayName("host house creation accepts the lower price boundary")
    void hostHouseCreate_priceZero_returnsOk() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        mockMvc.perform(post("/api/v1/house")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseCreateRequest("House A", "Seoul", 0L, "desc", 3)))
                .andExpect(status().isOk());

        verify(houseService).createHouse(any());
    }

    @Test
    @DisplayName("host house creation accepts max lengths and the upper price boundary")
    void hostHouseCreate_upperBoundaries_returnOk() throws Exception {
        MockHttpSession session = authenticatedHostSession();

        mockMvc.perform(post("/api/v1/house")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseCreateRequest(
                                "n".repeat(50),
                                "a".repeat(50),
                                1_000_000_000L,
                                "d".repeat(1000),
                                3
                        )))
                .andExpect(status().isOk());

        verify(houseService).createHouse(any());
    }

    @Test
    @DisplayName("host image upload returns success message and delegates to service")
    void hostImageUpload_success_returnsCurrentResponseContract() throws Exception {
        when(hostService.login(any())).thenReturn(sessionUser(2L, "host-user", "ROLE_HOST"));
        MockHttpSession session = signInAsHost();
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "house.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[] {1, 2, 3}
        );

        mockMvc.perform(multipart("/api/v1/house/1/image")
                        .file(imageFile)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image uploaded successfully."));

        verify(houseService).uploadHouseImage(eq(1L), any());
    }

    @Test
    @DisplayName("host image upload rejects empty file before service call")
    void hostImageUpload_emptyFile_returnsBadRequest() throws Exception {
        when(hostService.login(any())).thenReturn(sessionUser(2L, "host-user", "ROLE_HOST"));
        MockHttpSession session = signInAsHost();
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart("/api/v1/house/1/image")
                        .file(imageFile)
                        .session(session))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(houseService);
    }

    @Test
    @DisplayName("admin signin creates a session that can access admin-only endpoints")
    void adminSession_canAccessAdminEndpoint() throws Exception {
        when(adminService.login(any())).thenReturn(sessionUser(9L, "admin-user", "ROLE_ADMIN"));
        when(adminService.getHousesByStatus(HouseStatus.CREATE_PENDING)).thenReturn(List.of(
                House.builder()
                        .houseId(5L)
                        .hostId(2L)
                        .name("Pending House")
                        .address("Seoul")
                        .price(120000)
                        .maxGuests(4)
                        .description("pending")
                        .status(HouseStatus.CREATE_PENDING)
                        .build()
        ));

        MockHttpSession session = signInAsAdmin();

        mockMvc.perform(post("/api/v1/admin/manage")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "status": "CREATE_PENDING"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseId").value(5))
                .andExpect(jsonPath("$[0].status").value("CREATE_PENDING"));

        verify(adminService).getHousesByStatus(HouseStatus.CREATE_PENDING);
    }

    @Test
    @DisplayName("guest session cannot access admin endpoints")
    void nonAdminSession_cannotAccessAdminEndpoint() throws Exception {
        when(guestService.signInGuest(any())).thenReturn(sessionUser(1L, "guest-user", "ROLE_GUEST"));

        MockHttpSession session = signInAsGuest();

        mockMvc.perform(post("/api/v1/admin/manage")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "status": "CREATE_PENDING"
                            }
                            """))
                .andExpect(status().isForbidden());
    }

    private void expectCreateHouseBadRequest(
            MockHttpSession session,
            String name,
            String address,
            long price,
            String description,
            int maxGuests
    ) throws Exception {
        mockMvc.perform(post("/api/v1/house")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseCreateRequest(name, address, price, description, maxGuests)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(houseService);
    }

    private MockHttpSession authenticatedHostSession() throws Exception {
        when(hostService.login(any())).thenReturn(sessionUser(2L, "host-user", "ROLE_HOST"));
        return signInAsHost();
    }

    private String houseCreateRequest(String name, String address, long price, String description, int maxGuests) {
        return """
                {
                  "name": "%s",
                  "address": "%s",
                  "price": %d,
                  "description": "%s",
                  "maxGuests": %d
                }
                """.formatted(name, address, price, description, maxGuests);
    }

    private MockHttpSession signInAsGuest() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/guest/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "guest100",
                              "password": "Password1!"
                            }
                            """))
                .andExpect(status().isOk())
                .andReturn();

        return extractSession(result);
    }

    private MockHttpSession signInAsHost() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/host/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "host100",
                              "password": "Password1!"
                            }
                            """))
                .andExpect(status().isOk())
                .andReturn();

        return extractSession(result);
    }

    private MockHttpSession signInAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/admin/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "admin100",
                              "password": "Password1!"
                            }
                            """))
                .andExpect(status().isOk())
                .andReturn();

        return extractSession(result);
    }

    private MockHttpSession extractSession(MvcResult result) {
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);
        assertNotNull(session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
        return session;
    }

    private SessionUser sessionUser(Long id, String name, String role) {
        return SessionUser.builder()
                .id(id)
                .name(name)
                .role(role)
                .build();
    }
}