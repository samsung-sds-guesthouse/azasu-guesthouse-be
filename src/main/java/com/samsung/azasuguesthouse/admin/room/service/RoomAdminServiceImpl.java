package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.admin.common.util.ImageValidator;
import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.mapper.RoomAdminMapper;
import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.entity.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class RoomAdminServiceImpl implements RoomAdminService {

    private final RoomAdminMapper roomAdminMapper;

    public RoomAdminServiceImpl(RoomAdminMapper roomAdminMapper) {
        this.roomAdminMapper = roomAdminMapper;
    }

    @Transactional
    @Override
    public void registerRoom(RoomRequest dto) {
        // 1. 파일 유효성 검사 (DTO에서 꺼내 사용)
        ImageValidator.validate(dto.getPicture());

        // 2. DTO → Entity 변환
        Room room = toEntity(dto);

        // 3. DB 저장
        roomAdminMapper.insert(room);
    }

    private Room toEntity(RoomRequest dto) {
        Room room = new Room();
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPolicy(dto.getPolicy());
        room.setStatus(RoomStatus.INACTIVE);

        // getBytes() 중복 호출 제거 및 예외 처리 강화
        try {
            if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
                room.setPicture(dto.getPicture().getBytes());
            }
        } catch (IOException e) {
            throw new FileProcessingException("이미지 파일을 읽는 중 오류가 발생했습니다.", e);
        }

        return room;
    }
}
