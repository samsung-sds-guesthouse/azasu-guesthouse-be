package com.samsung.azasuguesthouse.guest.service;

import com.samsung.azasuguesthouse.common.cache.ReservationCache;
import com.samsung.azasuguesthouse.common.cache.RoomCache;
import com.samsung.azasuguesthouse.guest.dao.RoomMapper;
import com.samsung.azasuguesthouse.guest.dto.RoomDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final RoomCache roomCache;
    private final ReservationCache reservationCache;

    public RoomService(RoomMapper roomMapper, RoomCache roomCache, ReservationCache reservationCache) {
        this.roomMapper = roomMapper;
        this.roomCache = roomCache;
        this.reservationCache = reservationCache;
    }


    // 캐시에서 조회
    public RoomDto getRoomById(long roomId) {
        return roomCache.get(roomId);
    }


    // 캐시에서 조회
    public List<RoomDto> getAllRooms(LocalDate checkIn, LocalDate checkOut, Integer guestCount) {

        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);

        if (guestCount != null && (guestCount <= 0 || guestCount > 10)) {
            throw new IllegalArgumentException("인원 수는 1명 이상, 10명이하이어야 합니다.");
        }

        if (checkIn != null && (checkIn.isBefore(today.plusDays(1)) || checkIn.isAfter(oneMonthLater))) {
            throw new IllegalArgumentException("체크인은 내일부터 한 달 이내여야 합니다.");
        }

        if (checkOut != null && checkOut.isAfter(oneMonthLater)) {
            throw new IllegalArgumentException("체크아웃은 오늘로부터 한 달 이내여야 합니다.");
        }

        if (checkOut != null && !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜보다 나중이어야 합니다.");
        }


        List<RoomDto> rooms = roomCache.getAll();

        List<RoomDto> response = new ArrayList<>();
        for (RoomDto room : rooms) {
            if (guestCount != null && guestCount > room.getCapacity()) {
                continue;
            }
            if (checkIn != null && checkOut != null &&!reservationCache.isAvailable(room.getRoomId(), checkIn, checkOut)) {
                continue;
            }
            response.add(room);
        }

        return response;
    }
}
