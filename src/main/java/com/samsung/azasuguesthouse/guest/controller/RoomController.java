package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import com.samsung.azasuguesthouse.guest.service.ReservationService;
import com.samsung.azasuguesthouse.guest.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;
    private final ReservationService reservationService;

    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @Operation(summary = "객실 상세 정보 조회", description = "객실 ID를 기반으로 객실의 상세 정보를 조회합니다.")
    @GetMapping("/rooms/{id}")
    public ResponseEntity<SuccessResponse> getRoomById(
            @PathVariable("id") long roomId
            ) {

        SuccessResponse response = new SuccessResponse();
        response.putData("room", roomService.getRoomById(roomId));
        response.putData("reserved_dates", reservationService.getReservedDates(roomId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "비활성화 객실 상세 정보 조회", description = "객실 ID를 기반으로 객실의 상세 정보를 조회합니다.(로그인 필요)")
    @GetMapping("/rooms/inactive/{id}")
    public ResponseEntity<SuccessResponse> getInactiveRoomById(
            @PathVariable("id") long roomId,
            @AuthInfo Member member
    ) {

        long guestId = member.getId();

        SuccessResponse response = new SuccessResponse();
        response.putData("room", roomService.getInactiveRoom(roomId, guestId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @Operation(summary = "객실 검색", description = "날짜/인원수를 기준으로 객실 목록을 조회합니다.")
    @GetMapping("/rooms")
    public ResponseEntity<SuccessResponse> getAllRooms(
            @RequestParam(value = "check_in", required = false) LocalDate checkIn,
            @RequestParam(value = "check_out", required = false) LocalDate checkOut,
            @RequestParam(value = "guest_count", required = false) Integer guestCount
    ) {

        SuccessResponse response = new SuccessResponse();
        response.putData("rooms", roomService.getAllRooms(checkIn, checkOut, guestCount));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

}
