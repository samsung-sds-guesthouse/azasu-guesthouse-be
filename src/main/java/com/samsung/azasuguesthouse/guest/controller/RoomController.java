package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import com.samsung.azasuguesthouse.guest.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<SuccessResponse> getRoomById(
            @PathVariable("id") long roomId
    ) {
        SuccessResponse response = new SuccessResponse();
        response.putData("room", roomService.getRoomById(roomId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
