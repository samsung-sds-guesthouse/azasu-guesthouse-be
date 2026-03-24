package com.samsung.azasuguesthouse.admin.room.controller;

import com.samsung.azasuguesthouse.admin.room.dto.RoomModifyRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.dto.RoomResponse;
import com.samsung.azasuguesthouse.admin.room.service.RoomAdminService;
import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.log.Log;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Admin Room API", description = "관리자 전용 객실 관리 API")
@RestController
@RequestMapping("/api/v1/admin/rooms")
public class RoomAdminController {

    private final RoomAdminService roomAdminService;

    public RoomAdminController(RoomAdminService roomAdminService) {
        this.roomAdminService = roomAdminService;
    }

    @Operation(summary = "객실 등록", description = "새로운 객실 정보를 등록합니다. 이미지 파일(MultipartFile)을 포함할 수 있습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> addRoom(@AuthInfo Member member, @Valid @ModelAttribute RoomRequest roomDto) {
        Log.info("[admin:" + member.getId() + "] Request to add room: name=" + roomDto.getRoomName() + ", price=" + roomDto.getPrice());

        roomAdminService.registerRoom(roomDto);

        Log.info("[admin:" + member.getId() + "] Successfully registered room: " + roomDto.getRoomName());

        return ResponseEntity.ok(new SuccessResponse());
    }

    @Operation(summary = "전체 객실 조회", description = "등록된 모든 객실 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse> getAllRooms(@AuthInfo Member member) {
        Log.info("[admin:" + member.getId() + "] Request to fetch all rooms");

        List<RoomResponse> rooms = roomAdminService.getAllRooms();

        SuccessResponse response = new SuccessResponse();
        response.putData("rooms", rooms);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "객실 정보 수정", description = "기존 객실 정보를 수정합니다. 사진은 변경 시에만 전송합니다.")
    @PostMapping(value = "/{id}/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> modifyRoom(
            @AuthInfo Member member,
            @PathVariable long id,
            @Valid @ModelAttribute RoomModifyRequest modifyDto) {

        Log.info("[admin:" + member.getId() + "] Request to modify room id: " + id + ", name: " + modifyDto.getRoomName());

        roomAdminService.modifyRoom(id, modifyDto);

        return ResponseEntity.ok(new SuccessResponse()); // 성공 시 status 200, msg SUCCESS
    }

    @Operation(summary = "객실 활성화/비활성화", description = "객실의 예약 가능 상태를 변경합니다.")
    @PostMapping("/{id}/activation")
    public ResponseEntity<SuccessResponse> updateActivation(
            @Parameter(hidden = true) @AuthInfo Member member,
            @PathVariable long id,
            @RequestParam("is_active") boolean isActive) {

        Log.info("[admin:" + member.getId() + "] Request to set room id: " + id + " active=" + isActive);

        roomAdminService.updateActivation(id, isActive);

        return ResponseEntity.ok(new SuccessResponse());
    }
}