package com.samsung.azasuguesthouse.guest.dao;


import com.samsung.azasuguesthouse.guest.dto.ReservationDto;
import com.samsung.azasuguesthouse.guest.dto.ReservationRangeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {

    List<ReservationDto> findReservationsByGuestId(
            @Param("guestId") long guestId,
            @Param("offset") int offset,
            @Param("size") int size);

    int findTotalCountByGuestId(long guestId);

    List<ReservationRangeDto> findAllReservationRanges();
}