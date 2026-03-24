package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import com.samsung.azasuguesthouse.guest.service.ReservationService;
import com.samsung.azasuguesthouse.guest.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;
    private final ReservationService reservationService;

    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

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

    @GetMapping("/rooms")
    public void getAllRooms() {
        reservationService.getReservedDates(1L);
    }

}
