package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.guest.dto.PaginationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservingRequestDto;
import com.samsung.azasuguesthouse.guest.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations/me")
    public ResponseEntity<SuccessResponse> getMyReservations(@RequestParam int page) {
        long guestId = 2L; // session에서 가져와야됨

        SuccessResponse response = new SuccessResponse();
        response.putData("reservations", reservationService.getReservationsByGuestId(guestId, page));

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<SuccessResponse> createReservation(
            @RequestBody ReservingRequestDto requestDto
            ) {

            SuccessResponse response = new SuccessResponse();

            reservationService.makeReservation(requestDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
    }

    @PostMapping("/reservations/{id}/delete")
    public ResponseEntity<SuccessResponse> deleteReservation(
            @PathVariable("id") long id
    ) {
        SuccessResponse response = new SuccessResponse();

        reservationService.deleteReservationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
