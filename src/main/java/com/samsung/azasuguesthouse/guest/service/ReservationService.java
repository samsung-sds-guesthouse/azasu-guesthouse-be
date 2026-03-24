package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.guest.dao.ReservationMapper;
import com.samsung.azasuguesthouse.guest.dto.PaginationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservationRangeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationCache reservationCache;

    public ReservationService(ReservationMapper reservationMapper, ReservationCache reservationCache) {
        this.reservationMapper = reservationMapper;
        this.reservationCache = reservationCache;
    }

    public List<LocalDate> getReservedDates(Long roomId) {
        // 캐시가 비어있다면 전체 데이터 로딩
        if (reservationCache.isEmpty()) {
            System.out.println("캐시가 비어있습니다. DB에서 전체 데이터를 로드합니다.");
            loadAllRoomsToCache();
        }

        reservationCache.
                printAll();

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
}
