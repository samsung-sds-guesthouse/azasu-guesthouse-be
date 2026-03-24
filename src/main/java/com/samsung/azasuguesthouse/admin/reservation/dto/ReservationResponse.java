package com.samsung.azasuguesthouse.admin.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;

public class ReservationResponse {

    private Long id;

    @JsonProperty("guest_id")
    private Long guestId;

    @JsonProperty("guest_name")
    private String guestName;

    @JsonProperty("guest_phone")
    private String guestPhone;

    @JsonProperty("guest_count")
    private Integer guestCount;

    @JsonProperty("room_id")
    private Long roomId;

    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("check_in")
    private String checkIn;

    @JsonProperty("check_out")
    private String checkOut;

    private String status;

    @JsonProperty("total_price")
    private Integer totalPrice;

    private String picture; // Base64

    // MyBatis 매핑용 기본 생성자
    public ReservationResponse() {}

    // Getters
    public Long getId() { return id; }
    public Long getGuestId() { return guestId; }
    public String getGuestName() { return guestName; }
    public String getGuestPhone() { return guestPhone; }
    public Integer getGuestCount() { return guestCount; }
    public Long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public String getStatus() { return status; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getPicture() { return picture; }

    // Setters (MyBatis 매핑 필수)
    public void setId(Long id) { this.id = id; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(Integer totalPrice) { this.totalPrice = totalPrice; }

    // picture는 byte[]로 받아서 Base64로 변환
    public void setPictureBytes(byte[] pictureBytes) {
        if (pictureBytes != null) {
            this.picture = Base64.getEncoder().encodeToString(pictureBytes);
        }
    }
}