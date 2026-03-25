package com.samsung.azasuguesthouse.admin.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class RoomModifyRequest {

    @NotBlank(message = "객실 이름은 필수입니다.")
    @Size(min = 1, max = 20, message = "객실 이름은 1자 이상, 20자 이하")
    @Schema(description = "객실 이름", example = "아자수 1호실")
    private String roomName;

    @NotNull(message = "최소 인원은 필수입니다.")
    @Min(value = 1, message = "최소 1명 이상이어야 합니다.")
    @Max(value = 10, message = "최대 10명까지 가능합니다.")
    @Schema(description = "수용 인원", example = "2")
    private Integer capacity;

    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    @Schema(description = "가격", example = "150000")
    private Integer price;

    @NotBlank(message = "설명은 필수입니다.")
    @Size(min = 1, max = 2000, message = "설명은 1자 이상, 2000자 이하")
    private String description;

    @NotBlank(message = "이용 규칙은 필수입니다.")
    @Size(min = 1, max = 2000, message = "이용 규칙은 1자 이상, 2000자 이하")
    private String policy;

    @Schema(description = "객실 사진 파일 (변경 시에만 첨부)")
    private MultipartFile picture; // @NotNull 제거 (Nullable)

    // Getters and Setters
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }
    public MultipartFile getPicture() { return picture; }
    public void setPicture(MultipartFile picture) { this.picture = picture; }
}