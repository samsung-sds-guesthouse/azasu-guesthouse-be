package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.room.dto.RoomResponse;
import com.samsung.azasuguesthouse.common.cache.RoomCache;
import com.samsung.azasuguesthouse.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.admin.common.util.ImageValidator;
import com.samsung.azasuguesthouse.admin.room.dto.RoomDetailResponse;
import com.samsung.azasuguesthouse.admin.room.dto.RoomModifyRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.mapper.RoomAdminMapper;
import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.entity.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomAdminServiceImpl implements RoomAdminService {

    private final RoomAdminMapper roomAdminMapper;
    private final RoomCache roomCache;

    public RoomAdminServiceImpl(RoomAdminMapper roomAdminMapper, RoomCache roomCache) {
        this.roomAdminMapper = roomAdminMapper;
        this.roomCache = roomCache;
    }

    /**
     * 신규 객실 등록
     */
    @Transactional
    @Override
    public void registerRoom(RoomRequest dto) {
        // 1. 이미지 유효성 검사
        ImageValidator.validate(dto.getPicture());

        // 2. DTO -> Entity 변환 및 초기 상태 설정
        Room room = createRoomFromDto(dto);
        room.setStatus(RoomStatus.INACTIVE);

        // 3. 저장
        roomAdminMapper.insert(room);

        roomCache.clearAll();
    }

    /**
     * 전체 객실 목록 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<RoomResponse> getAllRooms() {
        return roomAdminMapper.findAll().stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 객실 상세 조회
     */
    @Transactional(readOnly = true)
    @Override
    public RoomDetailResponse getRoom(long id) {
        return new RoomDetailResponse(findRoomOrThrow(id));
    }

    /**
     * 객실 정보 수정
     */
    @Transactional
    @Override
    public void modifyRoom(long id, RoomModifyRequest dto) {
        // 1. 기존 데이터 존재 확인 (핵심: 수정 전 반드시 데이터가 있는지 확인)
        Room room = findRoomOrThrow(id);

        // 2. 이미지 처리 (새 이미지가 업로드된 경우에만)
        if (isImagePresent(dto.getPicture())) {
            ImageValidator.validate(dto.getPicture());
            room.setPicture(convertMultipartToBytes(dto.getPicture()));
        }

        // 3. 필드 값 업데이트 (Dirty Checking이 불가능한 Mapper 환경이므로 수동 세팅)
        updateRoomFields(room, dto);

        // 4. DB 반영
        roomAdminMapper.update(room);

        roomCache.clearAll();
    }

    /**
     * 객실 활성화/비활성화 상태 변경
     */
    @Transactional
    @Override
    public void updateActivation(long id, boolean isActive) {
        // 존재 확인 후 상태 변경
        findRoomOrThrow(id);

        RoomStatus status = isActive ? RoomStatus.ACTIVE : RoomStatus.INACTIVE;
        roomAdminMapper.updateStatus(id, status.name());

        roomCache.clearAll();
    }

    // --- Private Helper Methods (핵심 로직 분리) ---

    /**
     * ID로 객실을 조회하며, 없을 경우 일관된 예외를 던집니다.
     */
    private Room findRoomOrThrow(long id) {
        Room room = roomAdminMapper.findById(id);
        if (room == null) {
            throw new IllegalArgumentException("존재하지 않는 객실입니다. (ID: " + id + ")");
        }
        return room;
    }

    /**
     * MultipartFile을 byte[]로 변환하는 공통 로직
     */
    private byte[] convertMultipartToBytes(MultipartFile file) {
        try {
            return (file != null && !file.isEmpty()) ? file.getBytes() : null;
        } catch (IOException e) {
            throw new FileProcessingException("이미지 파일 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 이미지 파일이 실제로 존재하는지 확인
     */
    private boolean isImagePresent(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    /**
     * 신규 등록을 위한 Entity 생성 매핑
     */
    private Room createRoomFromDto(RoomRequest dto) {
        Room room = new Room();
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPolicy(dto.getPolicy());
        room.setPicture(convertMultipartToBytes(dto.getPicture()));
        return room;
    }

    /**
     * 기존 Entity의 필드 업데이트 로직 (수정 시 사용)
     */
    private void updateRoomFields(Room room, RoomModifyRequest dto) {
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPolicy(dto.getPolicy());
    }
}
