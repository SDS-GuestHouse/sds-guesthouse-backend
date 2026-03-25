package com.sds_guesthouse.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dto.house.HouseListResponseDto;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.util.auth.SessionUserProvider;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private HouseImageService houseImageService;

    @Mock
    private SessionUserProvider sessionUserProvider;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Test
    void createHouse_usesCurrentHostAndPendingStatus() {
        when(sessionUserProvider.getCurrentUserId()).thenReturn(7L);
        when(houseMapper.insertHouse(any())).thenReturn(1);

        houseService.createHouse(new HouseRequestDto("House A", "Seoul", 120000L, "desc", 3));

        ArgumentCaptor<House> captor = ArgumentCaptor.forClass(House.class);
        verify(houseMapper).insertHouse(captor.capture());

        House savedHouse = captor.getValue();
        assertEquals(7L, savedHouse.getHostId());
        assertEquals(HouseStatus.CREATE_PENDING, savedHouse.getStatus());
        assertEquals("House A", savedHouse.getName());
    }

    @Test
    void getMyHouses_returnsCurrentHostsHouses() {
        List<House> houses = List.of(House.builder().houseId(1L).hostId(7L).build());
        when(sessionUserProvider.getCurrentUserId()).thenReturn(7L);
        when(houseMapper.findByHostId(7L)).thenReturn(houses);

        assertSame(houses, houseService.getMyHouses());
        verify(houseMapper).findByHostId(7L);
    }

    @Test
    void getHouseImagePaths_returnsStoredPathsForExistingHouse() {
        List<String> imagePaths = List.of("uuid-a.jpg", "uuid-b.jpg");
        when(houseMapper.findById(3L)).thenReturn(House.builder().houseId(3L).hostId(2L).status(HouseStatus.APPROVED).build());
        when(houseImageService.getHouseImagePaths(3L)).thenReturn(imagePaths);

        assertSame(imagePaths, houseService.getHouseImagePaths(3L));
        verify(houseImageService).getHouseImagePaths(3L);
    }

    @Test
    void getHouseImagePaths_throwsWhenHouseIsMissing() {
        when(houseMapper.findById(9L)).thenReturn(null);

        ExplicitMessageException thrown = assertThrows(
                ExplicitMessageException.class,
                () -> houseService.getHouseImagePaths(9L)
        );

        assertEquals("House not found.", thrown.getMessage());
    }

    @Test
    void getAvailableHouses_normalizesInvalidPageAndUsesFixedPageSize() {
        List<House> houses = List.of(House.builder().houseId(1L).name("House A").build());
        when(houseMapper.countAvailableHouses(null, null, null, null)).thenReturn(1L);
        when(houseMapper.findAvailableHouses(null, null, null, null, 20, 0L)).thenReturn(houses);

        HouseListResponseDto response = houseService.getAvailableHouses(null, null, null, null, 0);

        assertSame(houses, response.getHouses());
        assertEquals(1, response.getPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(1L, response.getTotalCount());
        verify(houseMapper).countAvailableHouses(null, null, null, null);
        verify(houseMapper).findAvailableHouses(null, null, null, null, 20, 0L);
    }

    @Test
    void getAvailableHouses_returnsDifferentResultsForDifferentPages() {
        LocalDate startDate = LocalDate.of(2026, 4, 1);
        LocalDate endDate = LocalDate.of(2026, 4, 3);
        String location = "Seoul";
        Integer numberOfGuests = 4;

        List<House> firstPageHouses = LongStream.rangeClosed(1, 20)
                .mapToObj(id -> House.builder().houseId(id).name("House " + id).build())
                .toList();
        List<House> secondPageHouses = LongStream.rangeClosed(21, 25)
                .mapToObj(id -> House.builder().houseId(id).name("House " + id).build())
                .toList();

        when(houseMapper.countAvailableHouses(startDate, endDate, location, numberOfGuests)).thenReturn(25L);
        when(houseMapper.findAvailableHouses(startDate, endDate, location, numberOfGuests, 20, 0L)).thenReturn(firstPageHouses);
        when(houseMapper.findAvailableHouses(startDate, endDate, location, numberOfGuests, 20, 20L)).thenReturn(secondPageHouses);

        HouseListResponseDto firstPage = houseService.getAvailableHouses(startDate, endDate, location, numberOfGuests, 1);
        HouseListResponseDto secondPage = houseService.getAvailableHouses(startDate, endDate, location, numberOfGuests, 2);

        assertEquals(20, firstPage.getHouses().size());
        assertEquals(5, secondPage.getHouses().size());
        assertEquals(1, firstPage.getPage());
        assertEquals(2, secondPage.getPage());
        assertEquals(2, firstPage.getTotalPages());
        assertEquals(2, secondPage.getTotalPages());
        assertEquals(25L, firstPage.getTotalCount());
        assertEquals(25L, secondPage.getTotalCount());
        assertEquals(1L, firstPage.getHouses().get(0).getHouseId());
        assertEquals(21L, secondPage.getHouses().get(0).getHouseId());
        verify(houseMapper).findAvailableHouses(startDate, endDate, location, numberOfGuests, 20, 0L);
        verify(houseMapper).findAvailableHouses(startDate, endDate, location, numberOfGuests, 20, 20L);
    }

    @Test
    void getAvailableHouses_returnsEmptyResultWhenNoHouseMatches() {
        when(houseMapper.countAvailableHouses(null, null, "Busan", 2)).thenReturn(0L);

        HouseListResponseDto response = houseService.getAvailableHouses(null, null, "Busan", 2, 3);

        assertTrue(response.getHouses().isEmpty());
        assertEquals(3, response.getPage());
        assertEquals(0, response.getTotalPages());
        assertEquals(0L, response.getTotalCount());
        verify(houseMapper).countAvailableHouses(null, null, "Busan", 2);
        verifyNoMoreInteractions(houseMapper);
    }

    @Test
    void updateHouse_rejectsOtherHostsHouse() {
        when(sessionUserProvider.getCurrentUserId()).thenReturn(7L);
        when(houseMapper.findById(3L)).thenReturn(House.builder().houseId(3L).hostId(2L).status(HouseStatus.APPROVED).build());

        assertThrows(
                AccessDeniedException.class,
                () -> houseService.updateHouse(3L, new HouseRequestDto("House A", "Seoul", 120000L, "desc", 3))
        );
    }

    @Test
    void uploadHouseImage_delegatesForOwnedHouse() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "house.jpg", "image/jpeg", new byte[] {1, 2, 3});
        when(sessionUserProvider.getCurrentUserId()).thenReturn(7L);
        when(houseMapper.findById(5L)).thenReturn(House.builder().houseId(5L).hostId(7L).status(HouseStatus.APPROVED).build());

        houseService.uploadHouseImage(5L, file);

        verify(houseImageService).uploadHouseImage(5L, file);
    }
}