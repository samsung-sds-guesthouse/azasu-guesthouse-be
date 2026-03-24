package com.samsung.azasuguesthouse.admin.reservation.mapper;

import com.samsung.azasuguesthouse.admin.reservation.dto.ReservationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationAdminMapper {

    List<ReservationResponse> findAllWithDetails(@Param("offset") int offset, @Param("limit") int limit);

    int countAll();

    boolean existsById(@Param("id") long id);

    void updateStatus(@Param("id") long id, @Param("status") String status);
}