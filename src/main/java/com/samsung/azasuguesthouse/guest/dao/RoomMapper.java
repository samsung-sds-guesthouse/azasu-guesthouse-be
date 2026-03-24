package com.samsung.azasuguesthouse.guest.dao;

import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper {

    Room findById(long id);
    List<RoomDto> findAllRooms();
}
