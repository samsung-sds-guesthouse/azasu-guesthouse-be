package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.common.cache.RoomCache;
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
    private final RoomCache roomCache;

    public ReservationService(ReservationMapper reservationMapper, ReservationCache reservationCache, RoomCache roomCache, RoomService roomService) {
        this.reservationMapper = reservationMapper;
        this.reservationCache = reservationCache;
        this.roomCache = roomCache;
        this.roomService = roomService;
    }

    @Transactional(readOnly = true)
    public List<LocalDate> getReservedDates(Long roomId) {
        // 캐시가 비어있다면 전체 데이터 로딩
        if (reservationCache.isEmpty()) {
            System.out.println("캐시가 비어있습니다. DB에서 전체 데이터를 로드합니다.");
        }

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

//        System.out.println(offset + " " + size + " " + reservationMapper.findReservationsByGuestId(guestId, offset, size).size());

        return new PaginationDto<>(
                page,
                maxPage,
                reservationMapper.findReservationsByGuestId(guestId, offset, size)
        );
    }

    @Transactional
    public void makeReservation(ReservingRequestDto dto, long guestId) {

        // Lock
        reservationMapper.selectRoomForUpdate(dto.getRoomId());

        // 날짜 체크
        int overlappingCount = reservationMapper.checkAvailability(
                dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut()
        );
        if (overlappingCount > 0) {
            throw new IllegalStateException("이미 예약된 날짜가 포함되어 있습니다.");
        }

        // 인원 체크
        RoomDto roomDto = roomService.getRoomById(dto.getRoomId().longValue());
        if (roomDto == null) {
            throw new IllegalStateException("존재하지 않는 객실입니다.");
        }

        int capacity = roomDto.getCapacity();
        if (dto.getGuestCount() > capacity) {
            throw new IllegalStateException("예약 인원이 객실 최대 수용 인원을 초과합니다.");
        }

        // 가격 계산
        long nights = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        int totalPrice = roomDto.getPrice() * (int) nights;


        // DB 삽입
        reservationMapper.createReservation(guestId, dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut(), dto.getGuestCount(), totalPrice);

        // 캐시 삭제
        reservationCache.clearAll();
    }

    @Transactional
    public void deleteReservationById(long id, long guestId) {
        // DB 삭제
        reservationMapper.deleteReservation(id, guestId);

        // 캐시 삭제
        reservationCache.clearAll();
    }
}
