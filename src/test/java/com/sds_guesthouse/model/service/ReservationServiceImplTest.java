package com.sds_guesthouse.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dao.ReservationMapper;
import com.sds_guesthouse.model.dto.reservation.ReservationCreateRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;
import com.sds_guesthouse.util.auth.SessionUserProvider;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private SessionUserProvider sessionUserProvider;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void reserveHouse_usesCurrentGuestAndCalculatesTotalPrice() {
        LocalDate checkin = LocalDate.of(2026, 4, 10);
        LocalDate checkout = LocalDate.of(2026, 4, 12);
        when(sessionUserProvider.getCurrentUserId()).thenReturn(11L);
        when(houseMapper.findById(5L)).thenReturn(House.builder().houseId(5L).price(120000).status(HouseStatus.APPROVED).build());
        when(reservationMapper.countOverlappingActiveReservations(5L, checkin, checkout)).thenReturn(0);
        when(reservationMapper.insertReservation(any())).thenReturn(1);

        reservationService.reserveHouse(5L, new ReservationCreateRequestDto(checkin, checkout));

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationMapper).insertReservation(captor.capture());

        Reservation savedReservation = captor.getValue();
        assertEquals(11L, savedReservation.getGuestId());
        assertEquals(5L, savedReservation.getHouseId());
        assertEquals(ReservationStatus.PENDING, savedReservation.getStatus());
        assertEquals(240000, savedReservation.getTotalPrice());
    }

    @Test
    void getReservations_filtersByCurrentGuest() {
        List<Reservation> reservations = List.of(Reservation.builder().reservationId(9L).guestId(11L).build());
        when(sessionUserProvider.getCurrentUserId()).thenReturn(11L);
        when(reservationMapper.findReservationsByGuestId(11L, ReservationStatus.CONFIRMED, "2026-04-10")).thenReturn(reservations);

        assertSame(reservations, reservationService.getReservations(ReservationStatus.CONFIRMED, "2026-04-10"));
        verify(reservationMapper).findReservationsByGuestId(11L, ReservationStatus.CONFIRMED, "2026-04-10");
    }

    @Test
    void cancelReservation_updatesOnlyOwnedReservation() {
        when(sessionUserProvider.getCurrentUserId()).thenReturn(11L);
        when(reservationMapper.findReservationByReservationIdAndGuestId(8L, 11L)).thenReturn(
                Reservation.builder().reservationId(8L).guestId(11L).status(ReservationStatus.PENDING).build()
        );

        assertEquals("SUCCESS", reservationService.cancelReservation(8L));
        verify(reservationMapper).updateReservationStatus(8L, ReservationStatus.CANCELLED);
    }
}
