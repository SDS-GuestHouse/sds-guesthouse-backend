package com.sds_guesthouse.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import com.sds_guesthouse.model.dao.HouseMapper;
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
