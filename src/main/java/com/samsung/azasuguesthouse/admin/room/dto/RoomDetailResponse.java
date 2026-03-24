package com.samsung.azasuguesthouse.admin.room.dto;

import com.samsung.azasuguesthouse.entity.room.Room;

import java.time.LocalDateTime;
import java.util.Base64;

public class RoomDetailResponse {

    private Long id;
    private String roomName;
    private Integer capacity;
    private Integer price;
    private String description;
    private String policy;
    private String picture;     // Base64 인코딩
    private String status;      // ACTIVE / INACTIVE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoomDetailResponse(Room room) {
        this.id          = room.getId();
        this.roomName    = room.getRoomName();
        this.capacity    = room.getCapacity();
        this.price       = room.getPrice();
        this.description = room.getDescription();
        this.policy      = room.getPolicy();
        this.status      = room.getStatus().name();
        this.createdAt   = room.getCreatedAt();
        this.updatedAt   = room.getUpdatedAt();

        if (room.getPicture() != null) {
            this.picture = Base64.getEncoder().encodeToString(room.getPicture());
        }
    }

    public Long getId()                  { return id; }
    public String getRoomName()          { return roomName; }
    public Integer getCapacity()         { return capacity; }
    public Integer getPrice()            { return price; }
    public String getDescription()       { return description; }
    public String getPolicy()            { return policy; }
    public String getPicture()           { return picture; }
    public String getStatus()            { return status; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
}