package com.samsung.azasuguesthouse.entity.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private long id;
    private long guestId;      // 외래 키 (Member.id)
    private long roomId;       // 외래 키 (Room.id)
    private LocalDate checkIn;  // 체크인 날짜
    private LocalDate checkOut; // 체크아웃 날짜
    private int guestCount;     // 투숙 인원
    private int totalPrice;    // 총 결제 금액
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 1. 기본 생성자
    public Reservation() {
    }

    // 2. 전체 필드 생성자
    public Reservation(long id, long guestId, long roomId, LocalDate checkIn, LocalDate checkOut,
                       int guestCount, int totalPrice, ReservationStatus status,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 3. Getter / Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getGuestId() { return guestId; }
    public void setGuestId(long guestId) { this.guestId = guestId; }

    public long getRoomId() { return roomId; }
    public void setRoomId(long roomId) { this.roomId = roomId; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 4. toString()
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}
