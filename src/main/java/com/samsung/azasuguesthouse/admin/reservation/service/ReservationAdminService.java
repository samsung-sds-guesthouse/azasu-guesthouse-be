package com.samsung.azasuguesthouse.admin.reservation.service;

import com.samsung.azasuguesthouse.admin.reservation.dto.ReservationResponse;

import java.util.List;

public interface ReservationAdminService {

    List<ReservationResponse> getReservations(int page);

    int getMaxPage();

    void modifyStatus(long id, String status);
}