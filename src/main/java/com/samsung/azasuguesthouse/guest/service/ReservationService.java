package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.common.cache.RoomCache;
import com.samsung.azasuguesthouse.guest.dao.ReservationMapper;
import com.samsung.azasuguesthouse.guest.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<LocalDate> getReservedDates(Long roomId) {
        // 캐시가 비어있다면 전체 데이터 로딩
        if (reservationCache.isEmpty()) {
            System.out.println("캐시가 비어있습니다. DB에서 전체 데이터를 로드합니다.");
            loadAllRoomsToCache();
        }

        reservationCache.printAll();

        // 로딩 후 특정 방 데이터 반환 (없으면 빈 리스트 반환)
        return reservationCache.get(roomId) != null ?
                reservationCache.get(roomId) : Collections.emptyList();
    }

    // 전체 데이터를 DB에서 한 번에 가져와 캐시에 채움
    private synchronized void loadAllRoomsToCache() {
        // 다시 한 번 비어있는지 확인 (멀티쓰레드 방어)
        if (reservationCache.isEmpty()) {
            // DB에서 모든 방의 예약 날짜를 가져옴
            List<ReservationRangeDto> ranges = reservationMapper.findAllReservationRanges();

            Map<Long, List<LocalDate>> allData = new HashMap<>();

            for (ReservationRangeDto range : ranges) {
                List<LocalDate> dates = allData.computeIfAbsent(range.getRoomId(), k -> new ArrayList<>());

                // 체크인부터 체크아웃 전날까지 날짜 추가 (박 단위 계산)
                LocalDate current = range.getCheckIn();
                while (current.isBefore(range.getCheckOut())) {
                    dates.add(current);
                    current = current.plusDays(1);
                }
            }

            reservationCache.putAll(allData);
        }
    }

    @Transactional
    public void handleUpdate() {
        // DB 업데이트 후 캐시 전체 삭제
        reservationCache.clearAll();
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
    public void makeReservation(ReservingRequestDto dto) {
        // 본인 체크 필요

        // 가능한 날짜 체크 필요
        int overlappingCount = reservationMapper.checkAvailability(
                dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut()
        );

        if (overlappingCount > 0) {
            throw new IllegalStateException("선택하신 기간에 이미 예약이 존재합니다.");
        }

        // 인원 체크 필요
        int capacity = roomService.getRoomById(dto.getRoomId().longValue()).getCapacity();

//        RoomDto room = roomCache.get(dto.getRoomId().longValue());
//        if (room == null) {
//            throw new IllegalStateException("존재하지 않는 객실입니다.");
//        }
//
        if (dto.getGuestCount() > capacity) {
            throw new IllegalStateException("예약 인원이 객실 최대 수용 인원을 초과합니다.");
        }

        // DB 삽입
        reservationMapper.createReservation(2L, dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut(), dto.getGuestCount(), 20000);

        // 캐시 삭제
        reservationCache.clearAll();
    }

    @Transactional
    public void deleteReservationById(long id) {
        // 본인 체크 필요

        // DB 삭제
        reservationMapper.deleteReservation(id);

        // 캐시 삭제
        reservationCache.clearAll();
    }
}
