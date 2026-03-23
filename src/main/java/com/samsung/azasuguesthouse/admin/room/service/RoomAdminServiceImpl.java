package com.samsung.azasuguesthouse.admin.room.service;

import com.samsung.azasuguesthouse.admin.common.exception.FileProcessingException;
import com.samsung.azasuguesthouse.admin.common.util.ImageValidator;
import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.mapper.RoomAdminMapper;
import com.samsung.azasuguesthouse.entity.room.Room;
import com.samsung.azasuguesthouse.entity.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RoomAdminServiceImpl implements RoomAdminService {

    private final RoomAdminMapper roomAdminMapper;

    public RoomAdminServiceImpl(RoomAdminMapper roomAdminMapper) {
        this.roomAdminMapper = roomAdminMapper;
    }

    @Transactional
    @Override
    public void registerRoom(RoomRequest dto, MultipartFile file) throws IOException {
        // 1. 파일 유효성 검사 (크기, 확장자, Content-Type 모두 검증)
        ImageValidator.validate(file);

        // 2. DTO → Entity 변환
        Room room = toEntity(dto, file);

        // 3. DB 저장
        roomAdminMapper.insert(room);
    }

    /**
     * RoomRequest DTO와 이미지 파일을 Room 엔티티로 변환합니다.
     *
     * <p>변환 책임은 Service에 두는 것이 원칙입니다.
     * DTO가 Entity를 직접 생성하면 DTO와 Entity 사이의 결합이 생겨
     * 둘 중 하나가 바뀔 때 의도치 않은 영향이 생길 수 있습니다.
     *
     * <p><b>파일 저장 방식에 대한 주의:</b><br>
     * 현재는 이미지를 DB에 BLOB(byte[])으로 저장하고 있습니다.
     * 이 방식은 소규모 서비스에서는 단순하지만, 파일 수가 늘어나면
     * DB 크기 증가, 조회 성능 저하, 트랜잭션 크기 증가 등의 문제가 생깁니다.
     * 운영 환경에서는 S3(또는 NCP Object Storage) 업로드 후 URL만 저장하는
     * 방식으로 전환하는 것을 권장합니다.
     */
    private Room toEntity(RoomRequest dto, MultipartFile file) throws IOException {
        Room room = new Room();
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPolicy(dto.getPolicy());
        room.setStatus(RoomStatus.INACTIVE);
        room.setPicture(file.getBytes());

        try {
            if (file != null && !file.isEmpty()) {
                room.setPicture(file.getBytes());
            }
        } catch (IOException e) {
            throw new FileProcessingException("이미지 파일을 읽는 중 오류가 발생했습니다.", e);
        }

        return room;
    }
}
