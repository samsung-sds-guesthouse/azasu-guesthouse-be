package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.guest.dao.RoomMapper;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.springframework.stereotype.Service;


@Service
public class RoomService {

    private final RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
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
}
