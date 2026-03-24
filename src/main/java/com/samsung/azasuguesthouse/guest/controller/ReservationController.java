package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.guest.dto.PaginationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservingRequestDto;
import com.samsung.azasuguesthouse.guest.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<SuccessResponse> getMyReservations(@RequestParam int page, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
//        System.out.println(member.getId());
        long guestId = member.getId();

        SuccessResponse response = new SuccessResponse();
        response.putData("reservations", reservationService.getReservationsByGuestId(guestId, page));

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<SuccessResponse> createReservation(
            @RequestBody ReservingRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        long guestId = member.getId();

        SuccessResponse response = new SuccessResponse();

        reservationService.makeReservation(requestDto, guestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/reservations/{id}/delete")
    public ResponseEntity<SuccessResponse> deleteReservation(
            @PathVariable("id") long id,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        long guestId = member.getId();

        SuccessResponse response = new SuccessResponse();

        reservationService.deleteReservationById(id, guestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
