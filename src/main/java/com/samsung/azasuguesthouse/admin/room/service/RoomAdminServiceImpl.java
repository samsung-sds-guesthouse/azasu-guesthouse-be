package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.admin.common.util.ImageValidator;
import com.samsung.azasuguesthouse.admin.room.dto.RoomModifyRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomResponse;
import com.samsung.azasuguesthouse.admin.room.mapper.RoomAdminMapper;
import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.entity.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    @Override
    public List<RoomResponse> getAllRooms() {
        // 1. Mapper를 통해 모든 Room 엔티티 조회
        List<Room> rooms = roomAdminMapper.findAll();

        // 2. Entity 리스트를 RoomResponse(DTO) 리스트로 변환
        return rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void modifyRoom(long id, RoomModifyRequest dto) {
        // 1. 기존 데이터 존재 확인 (필요 시 추가)
        // 2. 사진이 전달된 경우에만 유효성 검사 수행
        if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
            ImageValidator.validate(dto.getPicture()); //
        }

        // 3. 엔티티 생성 및 데이터 세팅
        Room room = new Room();
        room.setId(id);
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPolicy(dto.getPolicy());

        // 4. 사진 처리 (전달된 경우만 byte[] 변환)
        try {
            if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
                room.setPicture(dto.getPicture().getBytes());
            }
        } catch (IOException e) {
            throw new FileProcessingException("이미지 파일을 읽는 중 오류가 발생했습니다.", e); //
        }

        // 5. DB 업데이트 실행
        roomAdminMapper.update(room); //
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
