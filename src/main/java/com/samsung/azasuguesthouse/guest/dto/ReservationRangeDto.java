package com.samsung.azasuguesthouse.guest.dto;


import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ReservationRangeDto {
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public ReservationRangeDto(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}
