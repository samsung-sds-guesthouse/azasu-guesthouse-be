package com.samsung.azasuguesthouse.common.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ReservationCache {

    private final Map<Long, List<LocalDate>> cache = new ConcurrentHashMap<>();

    public List<LocalDate> get(Long roomId) {
        return cache.get(roomId);
    }

    // 모든 데이터를 한꺼번에 캐시에 저장
    public void putAll(Map<Long, List<LocalDate>> allData) {
        cache.putAll(allData);
    }

    // 어떤 객실이라도 변경되면 캐시 초기화
    public void clearAll() {
        cache.clear();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public void printAll() {
        if (cache.isEmpty()) {
            System.out.println("캐시가 비어 있습니다.");
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
