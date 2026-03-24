package com.samsung.azasuguesthouse.admin.reservation.service;

import com.samsung.azasuguesthouse.admin.reservation.dto.ReservationResponse;
import com.samsung.azasuguesthouse.admin.reservation.mapper.ReservationAdminMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationAdminServiceImpl implements ReservationAdminService {

    private static final int PAGE_SIZE = 10;

    private final ReservationAdminMapper reservationAdminMapper;

    public ReservationAdminServiceImpl(ReservationAdminMapper reservationAdminMapper) {
        this.reservationAdminMapper = reservationAdminMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationResponse> getReservations(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        return reservationAdminMapper.findAllWithDetails(offset, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public int getMaxPage() {
        int total = reservationAdminMapper.countAll();
        return (int) Math.ceil((double) total / PAGE_SIZE);
    }
}