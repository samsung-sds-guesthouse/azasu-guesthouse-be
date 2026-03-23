package com.samsung.azasuguesthouse.admin.room.controller;

import com.samsung.azasuguesthouse.admin.room.dto.RoomRequest;
import com.samsung.azasuguesthouse.admin.room.service.RoomAdminService;
import com.samsung.azasuguesthouse.common.response.ExceptionResponse;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Room API", description = "관리자 전용 객실 관리 API")
@RestController
@RequestMapping("/api/v1/admin/rooms")
public class RoomAdminController {

    private static final Logger log = LoggerFactory.getLogger(RoomAdminController.class);

    private final RoomAdminService roomAdminService;

    public RoomAdminController(RoomAdminService roomAdminService) {
        this.roomAdminService = roomAdminService;
    }

    @Operation(summary = "객실 등록", description = "새로운 객실 정보를 등록합니다. 이미지 파일(MultipartFile)을 포함할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "객실 등록 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (검증 실패 등)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addRoom(
            @Valid @ModelAttribute RoomRequest roomDto,
            BindingResult bindingResult) {

        log.info("Request to add room received. Room Name: {}, Capacity: {}, Price: {}", 
                roomDto.getRoomName(), roomDto.getCapacity(), roomDto.getPrice());

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(new ExceptionResponse(400, "Validation failed"));
        }

        try {
            roomAdminService.registerRoom(roomDto, roomDto.getPicture());
            log.info("Successfully registered room: {}", roomDto.getRoomName());
            return ResponseEntity.ok(new SuccessResponse());
        } catch (Exception e) {
            log.error("Error occurred while registering room: {}", roomDto.getRoomName(), e);
            return ResponseEntity.badRequest().body(new ExceptionResponse(400, "FAIL"));
        }
    }
}
