package com.samsung.azasuguesthouse.admin.reservation.service;

import com.samsung.azasuguesthouse.admin.reservation.dto.ReservationResponse;
import com.samsung.azasuguesthouse.admin.reservation.mapper.ReservationAdminMapper;
import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.entity.reservation.ReservationStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationAdminServiceImpl implements ReservationAdminService {

    private static final int PAGE_SIZE = 10;

    private final ReservationAdminMapper reservationAdminMapper;
    private final ReservationCache reservationCache;

    public ReservationAdminServiceImpl(ReservationAdminMapper reservationAdminMapper, ReservationCache reservationCache) {
        this.reservationAdminMapper = reservationAdminMapper;
        this.reservationCache = reservationCache;
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

    @Transactional
    @Override
    public void modifyStatus(long id, String status) {
        ReservationStatus reservationStatus;
        try {
            reservationStatus = ReservationStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        if (!reservationAdminMapper.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found: " + id);
        }

        reservationAdminMapper.updateStatus(id, reservationStatus.name());

        reservationCache.clearAll();
    }
}