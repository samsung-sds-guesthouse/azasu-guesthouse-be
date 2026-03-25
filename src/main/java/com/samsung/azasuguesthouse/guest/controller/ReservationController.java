package com.samsung.azasuguesthouse.guest.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.guest.dto.PaginationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservingRequestDto;
import com.samsung.azasuguesthouse.guest.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "내 예약 목록 조회", description = "내 예약 목록을 페이지 단위(10개)로 조회합니다.")
    @GetMapping("/reservations/me")
    public ResponseEntity<SuccessResponse> getMyReservations(
            @RequestParam int page,
            @AuthInfo Member member) {

        SuccessResponse response = new SuccessResponse();
        response.putData("reservations", reservationService.getReservationsByGuestId(member.getId(), page));

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);
    }

    @Operation(summary = "예약 생성하기", description = "객실 예약을 생성합니다.")
    @PostMapping("/reservations")
    public ResponseEntity<SuccessResponse> createReservation(
            @RequestBody ReservingRequestDto requestDto,
            @AuthInfo Member member
    ) {

        SuccessResponse response = new SuccessResponse();

        reservationService.makeReservation(requestDto, member.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "예약 취소하기", description = "객실 예약을 취소합니다.")
    @PostMapping("/reservations/{id}/delete")
    public ResponseEntity<SuccessResponse> deleteReservation(
            @PathVariable("id") long id,
            @AuthInfo Member member
    ) {

        SuccessResponse response = new SuccessResponse();
        reservationService.deleteReservationById(id, member.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
