package com.samsung.azasuguesthouse.guest.dao;


import com.samsung.azasuguesthouse.guest.dto.ReservationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {

    List<ReservationDto> findReservationsByGuestId(
            @Param("guestId") long guestId,
            @Param("offset") int offset,
            @Param("size") int size);

    int findTotalCountByGuestId(long guestId);
}