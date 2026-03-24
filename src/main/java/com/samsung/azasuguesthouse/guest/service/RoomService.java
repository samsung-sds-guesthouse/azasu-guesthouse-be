package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.RoomCache;
import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.guest.dao.RoomMapper;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final RoomCache roomCache;

    public RoomService(RoomMapper roomMapper, RoomCache roomCache) {
        this.roomMapper = roomMapper;
        this.roomCache = roomCache;
    }

    public RoomDto getRoomById(long roomId) {

        Room room = roomMapper.findById(roomId);

        return new RoomDto(
                room.getId(),
                room.getRoomName(),
                room.getPrice(),
                room.getCapacity(),
                room.getPicture(),
                room.getDescription(),
                room.getPolicy()
        );

    }

    public List<RoomDto> getAllRooms() {
        if (roomCache.isEmpty()) {
            System.out.println("객실 캐시가 비어있습니다. DB에서 전체 데이터를 로드합니다.");
            loadAllRoomsToCache();
        }

        roomCache.printAll();

        return roomCache.getAll();
    }

    private synchronized void loadAllRoomsToCache() {
        // 다시 한 번 비어있는지 확인 (멀티쓰레드 방어)
        if (roomCache.isEmpty()) {
            // DB에서 모든 객실 정보를 가져옴
            List<RoomDto> rooms = roomMapper.findAllRooms();

            // List를 Map<Long, RoomDto> 구조로 변환
            Map<Long, RoomDto> allData = rooms.stream()
                    .collect(Collectors.toMap(
                            RoomDto::getRoomId, // Key: 객실 ID
                            room -> room    // Value: 객실 객체
                    ));

            roomCache.putAll(allData);
        }
    }
}
