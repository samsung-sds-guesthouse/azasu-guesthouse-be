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

        List<RoomDto> rooms = roomCache.getAll();

        List<RoomDto> response = new ArrayList<>();
        for (RoomDto room : rooms) {
            if (guestCount != null && guestCount > room.getCapacity()) {
                continue;
            }
            if (checkIn != null && checkOut != null
                    && !reservationCache.isAvailable(room.getRoomId(), checkIn, checkOut)) {
                continue;
            }
            response.add(room);
        }

        return response;
    }

//    public void checkGuestCount(List<RoomDto> roomDtos, Integer guestCount) {
//        if (guestCount != null) {
//            roomDtos.removeIf(roomDto -> guestCount > roomDto.getCapacity());
//        }
//    }
//
//    public void checkDateRange(List<RoomDto> roomDtos, LocalDate checkIn, LocalDate checkOut) {
//        if (checkIn != null && checkOut != null) {
//            roomDtos.removeIf(roomDto -> reservationCache.isAvailable(roomDto.getRoomId(), checkIn, checkOut));
//        }
//    }

}
