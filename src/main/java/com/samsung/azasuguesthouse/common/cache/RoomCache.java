package com.samsung.azasuguesthouse.common.cache;

import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomCache {

    private final Map<Long, RoomDto> cache = new ConcurrentHashMap<>();

    // 특정 객실 정보 가져오기
    public RoomDto get(Long roomId) {
        return cache.get(roomId);
    }

    // 전체 객실 목록 가져오기
    public List<RoomDto> getAll() {
        return new ArrayList<>(cache.values());
    }

    public void putAll(Map<Long, RoomDto> allRooms) {
        cache.putAll(allRooms);
    }

    // 캐시 전체 초기화
    public void clearAll() {
        cache.clear();
    }

    // 캐시가 비어있는지 확인
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public void printAll() {
        if (cache.isEmpty()) {
            System.out.println("객실 캐시가 비어 있습니다.");
            return;
        }

        System.out.println("=== 전체 객실 정보 캐시 현황 ===");
        cache.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("객실 ID: %d | 객실 정보: %s%n",
                            entry.getKey(), entry.getValue());
                });
        System.out.println("===============================");
    }
}
