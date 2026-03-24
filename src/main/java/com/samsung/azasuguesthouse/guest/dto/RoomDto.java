package com.samsung.azasuguesthouse.guest.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomDto {

    private long roomId;
    private String roomName;
    private Integer price;
    private Integer capacity;
    private byte[] picture;
    private String description;
    private String policy;

//    public RoomDto() {
//    }

    public RoomDto(long roomId,String roomName, Integer price, Integer capacity,
                   byte[] picture, String description, String policy) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.capacity = capacity;
        this.picture = picture;
        this.description = description;
        this.policy = policy;
    }

    public long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public Integer getPrice() { return price; }
    public Integer getCapacity() { return capacity; }
    public byte[] getPicture() { return picture; }
    public String getDescription() { return description; }
    public String getPolicy() { return policy; }

//    public void setRoomId(Integer roomId) { this.roomId = roomId; }
//    public void setRoomName(String roomName) { this.roomName = roomName; }
//    public void setPrice(Integer price) { this.price = price; }
//    public void setCapacity(Integer capacity) { this.capacity = capacity; }
//    public void setPicture(String picture) { this.picture = picture; }
//    public void setDescription(String description) { this.description = description; }
//    public void setPolicy(String policy) { this.policy = policy; }
}