package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RoomAdminService {
    @Transactional
    void registerRoom(RoomRequest dto, MultipartFile file) throws IOException;
}
