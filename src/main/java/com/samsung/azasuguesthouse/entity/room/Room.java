package com.samsung.azasuguesthouse.entity.room;

import java.time.LocalDateTime;

public class Room {

    private long id;
    private String roomName;
    private int capacity;
    private int price;
    private String description;
    private String policy;
    private byte[] picture; // MEDIUMBLOB 매핑
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 1. 기본 생성자 (MyBatis 필수)
    public Room() {
    }

    // 2. 전체 필드 생성자 (선택 사항)
    public Room(long id, String roomName, int capacity, int price, String description,
                String policy, byte[] picture, RoomStatus status,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
        this.policy = policy;
        this.picture = picture;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 3. Getter / Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }

    public byte[] getPicture() { return picture; }
    public void setPicture(byte[] picture) { this.picture = picture; }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 4. toString() (디버깅용, picture는 크기가 클 수 있어 길이를 출력하도록 설정)
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", status=" + status +
                ", pictureSize=" + (picture != null ? picture.length : 0) +
                ", createdAt=" + createdAt +
                '}';
    }
}
