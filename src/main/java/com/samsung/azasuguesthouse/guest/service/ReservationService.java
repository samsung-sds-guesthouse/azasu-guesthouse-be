package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.guest.dao.ReservationMapper;
import com.samsung.azasuguesthouse.guest.dto.PaginationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Transactional(readOnly = true)
    public PaginationDto getReservationsByGuestId(long guestId, int page) {

        int size = 10; // Default 10개
        int totalCount = reservationMapper.findTotalCountByGuestId(guestId);

        int maxPage = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / size);

        if (page < 1) {
            page = 1;
        }

        if (page > maxPage) {
            page = maxPage;
        }

        int offset = (page - 1) * size;

//        System.out.println(offset + " " + size + " " + reservationMapper.findReservationsByGuestId(guestId, offset, size).size());

        return new PaginationDto<>(
                page,
                maxPage,
                reservationMapper.findReservationsByGuestId(guestId, offset, size)
        );
    }
}
