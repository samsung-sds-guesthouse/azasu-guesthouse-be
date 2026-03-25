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

    // 1. roomCache에서 가져와야 한다.
    // 2. roomCache를 업데이트 하는 것은 roomCache 객체의 역할이다.
    // 3. roomCache.get()을 사용하고 reload는 roomCache 내부에서 관리한다.
    // 4. roomCache가 변경되는 것은 관리자가 객실 정보를 수정할 때이다.
    // 5. 날짜, 인원 수 조건 검색은 reservationCache, roomCache를 활용한다.


    public RoomDto getRoomById(long roomId) {

//        Room room = roomMapper.findById(roomId);
//
//        return new RoomDto(
//                room.getId(),
//                room.getRoomName(),
//                room.getPrice(),
//                room.getCapacity(),
//                room.getPicture(),
//                room.getDescription(),
//                room.getPolicy()
//        );

        return roomCache.get(roomId);

    }


    public List<RoomDto> getAllRooms() {

        return roomCache.getAll();
    }
}
