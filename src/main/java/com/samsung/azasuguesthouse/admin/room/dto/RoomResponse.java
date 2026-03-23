package com.samsung.azasuguesthouse.admin.room.dto;

import com.samsung.azasuguesthouse.entity.room.Room;

import java.util.Base64;

public class RoomResponse {
    private String status;
    private Long roomId;
    private String roomName;

    private Integer price;
    private Integer capacity;
    private String picture; // Base64로 인코딩된 문자열

    public RoomResponse(Room room) {
        this.status = room.getStatus().name();
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.price = room.getPrice();
        this.capacity = room.getCapacity();

        // 이미지가 있을 경우 Base64로 인코딩
        if (room.getPicture() != null) {
            this.picture = Base64.getEncoder().encodeToString(room.getPicture());
        }
    }

    // Getters
    public String getStatus() { return status; }
    public Long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public Integer getPrice() { return price; }
    public Integer getCapacity() { return capacity; }
    public String getPicture() { return picture; }
}
