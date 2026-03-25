package com.samsung.azasuguesthouse.common.cache;

import com.samsung.azasuguesthouse.guest.dao.RoomMapper;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomCache {

    private final Map<Long, RoomDto> cache = new ConcurrentHashMap<>();
    private final RoomMapper roomMapper;

    public RoomCache(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }


    public RoomDto get(Long roomId) {
        checkAndReload();
        return cache.get(roomId);
    }

    public List<RoomDto> getAll() {
        checkAndReload();
        return new ArrayList<>(cache.values());
    }

    private void checkAndReload() {
        if (cache.isEmpty()) {
            synchronized (this) {
                if (cache.isEmpty()) {
                    reload();
                }
            }
        }
    }

    public void reload() {
        synchronized (this) {

            System.out.println("[Room] 캐시 로딩을 시작합니다.");

            List<RoomDto> rooms = roomMapper.findAllRooms();

            if (rooms == null || rooms.isEmpty()) {
                System.out.println("[Room] DB에 저장된 객실 정보가 없습니다.");
                return;
            }

            Map<Long, RoomDto> tempMap = rooms.stream()
                    .collect(Collectors.toMap(RoomDto::getRoomId, Function.identity()));

            cache.clear();
            cache.putAll(tempMap);

            printAll();
        }
    }


    public void clearAll() {
        cache.clear();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }


    public void printAll() {
        if (cache.isEmpty()) {
            System.out.println("[Room] 캐시가 비어 있습니다.");
            return;
        }

        System.out.println("=== 전체 객실 정보 캐시 현황 ===");
        cache.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("객실 ID: %d | 객실명: %s | 정보: %s%n",
                            entry.getKey(), entry.getValue().getRoomName(), entry.getValue());
                });
        System.out.println("===============================");
    }
}
