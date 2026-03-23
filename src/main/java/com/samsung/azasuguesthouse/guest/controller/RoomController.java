package com.samsung.azasuguesthouse.guest.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(
            @PathVariable("id") long roomId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomById(roomId));
    }

}
