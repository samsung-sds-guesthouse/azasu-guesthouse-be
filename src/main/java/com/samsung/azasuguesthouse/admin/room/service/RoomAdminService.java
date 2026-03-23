package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomResponse;

import java.util.List;


public interface RoomAdminService {
    void registerRoom(RoomRequest dto);
    List<RoomResponse> getAllRooms();
}
