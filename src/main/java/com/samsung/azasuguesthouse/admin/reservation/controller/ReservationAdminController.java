package com.samsung.azasuguesthouse.admin.reservation.controller;

import com.samsung.azasuguesthouse.admin.reservation.dto.ReservationResponse;
import com.samsung.azasuguesthouse.admin.reservation.service.ReservationAdminService;
import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.log.Log;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin Reservation API", description = "관리자 전용 예약 관리 API")
@RestController
@RequestMapping("/api/v1/admin/reservations")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    public ReservationAdminController(ReservationAdminService reservationAdminService) {
        this.reservationAdminService = reservationAdminService;
    }

    @Operation(summary = "예약 목록 조회", description = "전체 예약 목록을 페이지 단위(10개)로 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse> getReservations(
            @AuthInfo Member member,
            @RequestParam("page") int page) {

        Log.info("[admin:" + member.getId() + "] Request to fetch reservations, page=" + page);

        List<ReservationResponse> reservations = reservationAdminService.getReservations(page);
        int maxPage = reservationAdminService.getMaxPage();

        SuccessResponse response = new SuccessResponse();
        response.putData("reservations", reservations);
        response.putData("current_page", page);
        response.putData("max_page", maxPage);

        return ResponseEntity.ok(response);
    }
}