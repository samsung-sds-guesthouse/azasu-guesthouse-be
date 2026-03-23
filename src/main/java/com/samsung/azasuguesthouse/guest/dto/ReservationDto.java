package com.samsung.azasuguesthouse.guest.dto;

import com.samsung.azasuguesthouse.entity.reservation.ReservationStatus;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationDto {

    private long id;
    private long guestId;
    private long roomId;
    private String roomName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guestCount;
    private int totalPrice;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private byte[] picture;

    public ReservationDto(long id,
                          long guestId,
                          long roomId,
                          String roomName,
                          LocalDate checkIn,
                          LocalDate checkOut,
                          int guestCount,
                          int totalPrice,
                          ReservationStatus status,
                          byte[] picture,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
        this.status = status;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public long getGuestId() { return guestId; }
    public long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getGuestCount() { return guestCount; }
    public int getTotalPrice() { return totalPrice; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public byte[] getPicture() { return picture; }
}
