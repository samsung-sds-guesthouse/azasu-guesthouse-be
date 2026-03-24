package com.samsung.azasuguesthouse.common.cache;

import com.samsung.azasuguesthouse.guest.dao.ReservationMapper;
import com.samsung.azasuguesthouse.guest.dto.ReservationRangeDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class ReservationCache {

    private final Map<Long, List<LocalDate>> cache = new ConcurrentHashMap<>();
    private final ReservationMapper reservationMapper;

    private ReservationCache(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public List<LocalDate> get(Long roomId) {
        if (cache.isEmpty()) {
            synchronized (this) { // 동시성 관리
                if (cache.isEmpty()) {
                    printAll();
                    reload();
                }
            }
        }
        return cache.getOrDefault(roomId, Collections.emptyList());
    }

    private void reload() {
        synchronized (this) {
            if (!cache.isEmpty()) {
                return;
            }
            System.out.println("[Reservation] 캐시 로딩을 시작합니다.");

            List<ReservationRangeDto> ranges = reservationMapper.findAllReservationRanges();

            Map<Long, List<LocalDate>> tempMap = new HashMap<>();

            for (ReservationRangeDto range : ranges) {
                List<LocalDate> dates = range.getCheckIn()
                        .datesUntil(range.getCheckOut())
                        .toList();

                tempMap.computeIfAbsent(range.getRoomId(), k -> new ArrayList<>())
                        .addAll(dates);
            }

            cache.putAll(tempMap);

            printAll();
        }
    }

    // 모든 데이터를 한꺼번에 캐시에 저장
//    public void putAll(Map<Long, List<LocalDate>> allData) {
//        cache.putAll(allData);
//    }

    // 어떤 객실이라도 변경되면 캐시 초기화
    public void clearAll() {
        cache.clear();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean isAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<LocalDate> reservedDates = this.get(roomId);

        if (reservedDates == null || reservedDates.isEmpty()) {
            return true;
        }

        return checkIn.datesUntil(checkOut)
                .noneMatch(reservedDates::contains);
    }

    public void printAll() {
        if (cache.isEmpty()) {
            System.out.println("[Reservation] 캐시가 비어 있습니다.");
            return;
        }

        System.out.println("=== 전체 객실 예약 캐시 현황 ===");
        cache.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("객실 ID: %d | 예약 날짜 목록: %s%n",
                            entry.getKey(), entry.getValue());
                });
        System.out.println("==============================");
    }

}
