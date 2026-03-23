package com.samsung.azasuguesthouse.admin.room.mapper;

import com.samsung.azasuguesthouse.entity.room.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoomAdminMapper {
    void insert(Room room);

    Room findById(Long id);

    List<Room> findAll();

    void update(Room room);

    void updateStatus(@Param("id") Long id, @Param("status") String status);

    void delete(Long id);
}
