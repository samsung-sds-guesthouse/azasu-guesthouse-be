package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.common.log.Log;
import com.samsung.azasuguesthouse.guest.dao.ReservationMapper;
import com.samsung.azasuguesthouse.guest.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationService {

    private final RoomService roomService;

    private final ReservationMapper reservationMapper;
    private final ReservationCache reservationCache;

    public ReservationService(ReservationMapper reservationMapper, ReservationCache reservationCache, RoomService roomService) {
        this.reservationMapper = reservationMapper;
        this.reservationCache = reservationCache;
        this.roomService = roomService;
    }

    @Transactional(readOnly = true)
    public List<LocalDate> getReservedDates(Long roomId) {

        // 로딩 후 특정 방 데이터 반환 (없으면 빈 리스트 반환)
        return reservationCache.get(roomId) != null ?
                reservationCache.get(roomId) : Collections.emptyList();
    }


    @Transactional(readOnly = true)
    public PaginationDto<ReservationDto> getReservationsByGuestId(long guestId, int page) {

        int size = 10; // Default 10개
        int totalCount = reservationMapper.findTotalCountByGuestId(guestId);

        int maxPage = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / size);

        if (page < 1) {
            page = 1;
        }
        if (page > maxPage) {
            page = maxPage;
        }

        int offset = (page - 1) * size;

        return new PaginationDto<>(
                page,
                maxPage,
                reservationMapper.findReservationsByGuestId(guestId, offset, size)
        );
    }

    @Transactional
    public void makeReservation(ReservingRequestDto dto, long guestId) {

        // Lock
        RoomDto roomDto = reservationMapper.selectRoomForUpdate(dto.getRoomId());

        // 날짜 체크
        LocalDate today = LocalDate.now();
        LocalDate checkIn = dto.getCheckIn();
        LocalDate checkOut = dto.getCheckOut();

        if (checkIn.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("체크인은 내일(" + today.plusDays(1) + ")부터 가능합니다.");
        }

        LocalDate maxDate = today.plusMonths(1);
        if (checkOut.isAfter(maxDate)) {
            throw new IllegalArgumentException("예약은 오늘로부터 1개월 이내(" + maxDate + ")까지만 가능합니다.");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜보다 나중이어야 합니다.");
        }

        int overlappingCount = reservationMapper.checkAvailability(dto.getRoomId(), checkIn, checkOut);
        if (overlappingCount > 0) {
            throw new IllegalStateException("이미 예약된 날짜가 포함되어 있습니다.");
        }

        // 인원 체크
//        RoomDto roomDto = roomService.getRoomById(dto.getRoomId().longValue());
        if (roomDto == null) {
            throw new IllegalStateException("존재하지 않는 객실입니다.");
        }

        int capacity = roomDto.getCapacity();

        if (dto.getGuestCount() < 1) {
            throw new IllegalArgumentException("예약 인원은 최소 1명 이상이어야 합니다.");
        }

        if (dto.getGuestCount() > capacity) {
            throw new IllegalStateException("예약 인원이 객실 최대 수용 인원을 초과합니다.");
        }

        // 가격 계산
        long nights = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        int totalPrice;

        try {
            long totalLong = Math.multiplyExact((long) roomDto.getPrice(), nights);

            // 이후 int 범위를 넘는지 체크하거나, 아예 totalPrice를 long으로 유지
            if (totalLong > Integer.MAX_VALUE) {
                throw new ArithmeticException("결제 금액이 제한 범위를 초과했습니다.");
            }
            totalPrice = (int) totalLong;

        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("예약 기간 또는 금액이 너무 커서 처리할 수 없습니다.");
        }


        // DB 삽입
        reservationMapper.createReservation(guestId, dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut(), dto.getGuestCount(), totalPrice);

        String message = String.format(
                "[Guest] 새로운 예약이 생성되었습니다. Guest ID: %s, Room ID: %s, Check-in: %s, Check-out: %s, Guest Count: %d, Total Price: %d",
                guestId, dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut(), dto.getGuestCount(), totalPrice
        );

        Log.guest(message);

        // 캐시 삭제
        reservationCache.clearAll();
    }

    @Transactional
    public void deleteReservationById(long id, long guestId) {
        // DB 삭제
        reservationMapper.deleteReservation(id, guestId);

        String message = String.format(
                "[Guest] 예약이 취소되었습니다. Reservation ID: %s, Guest ID: %s",
                id, guestId
        );
        Log.guest(message);

        // 캐시 삭제
        reservationCache.clearAll();
    }
}
