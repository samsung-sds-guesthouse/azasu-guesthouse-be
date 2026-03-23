package com.samsung.azasuguesthouse.guest.dao;

import com.samsung.azasuguesthouse.entity.room.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {

    Room findById(long id);
}
