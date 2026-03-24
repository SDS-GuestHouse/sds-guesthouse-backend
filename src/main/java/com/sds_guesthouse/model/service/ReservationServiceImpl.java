package com.sds_guesthouse.model.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dao.ReservationMapper;
import com.sds_guesthouse.model.dto.reservation.ReservationCreateRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.model.entity.ReservationStatus;
import com.sds_guesthouse.util.auth.SessionUserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final HouseMapper houseMapper;
    private final SessionUserProvider sessionUserProvider;

    @Override
    @Transactional
    public void reserveHouse(Long houseId, ReservationCreateRequestDto dto) {
        House house = houseMapper.findById(houseId);
        if (house == null) {
            throw new ExplicitMessageException("House not found.");
        }
        if (house.getStatus() != HouseStatus.APPROVED) {
            throw new ExplicitMessageException("This house cannot be reserved.");
        }

        int overlapCount = reservationMapper.countOverlappingActiveReservations(
                houseId,
                dto.getCheckinDate(),
                dto.getCheckoutDate()
        );
        if (overlapCount > 0) {
            throw new ExplicitMessageException("The requested dates are already reserved.");
        }

        long nights = ChronoUnit.DAYS.between(dto.getCheckinDate(), dto.getCheckoutDate());
        if (nights <= 0) {
            throw new ExplicitMessageException("checkoutDate must be after checkinDate.");
        }

        Reservation reservation = Reservation.builder()
                .guestId(sessionUserProvider.getCurrentUserId())
                .houseId(houseId)
                .status(ReservationStatus.PENDING)
                .checkinDate(dto.getCheckinDate())
                .checkoutDate(dto.getCheckoutDate())
                .totalPrice(Math.toIntExact(nights * house.getPrice()))
                .build();

        if (reservationMapper.insertReservation(reservation) == 0) {
            throw new ExplicitMessageException("Failed to create reservation.");
        }
    }

    @Override
    public List<Reservation> getReservations(ReservationStatus status, String date) {
        return reservationMapper.findReservationsByGuestId(sessionUserProvider.getCurrentUserId(), status, date);
    }

    @Override
    public Reservation getReservationByReservationId(Long reservationId) {
        Reservation reservation = reservationMapper.findReservationByReservationIdAndGuestId(
                reservationId,
                sessionUserProvider.getCurrentUserId()
        );
        if (reservation == null) {
            throw new ExplicitMessageException("Reservation not found.");
        }
        return reservation;
    }

    @Override
    @Transactional
    public String cancelReservation(Long reservationId) {
        Reservation reservation = reservationMapper.findReservationByReservationIdAndGuestId(
                reservationId,
                sessionUserProvider.getCurrentUserId()
        );
        if (reservation == null) {
            return "NOT_FOUND";
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return "ALREADY_CANCELLED";
        }
        reservationMapper.updateReservationStatus(reservationId, ReservationStatus.CANCELLED);
        return "SUCCESS";
    }
}
