package com.samsung.azasuguesthouse.entity.member;

import java.time.LocalDateTime;

public class Salt {

    private long memberId;
    private String salt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Salt(long memberId, String salt) {
        this.memberId = memberId;
        this.salt = salt;
    }

    public long getMemberId() { return memberId; }
    public void setMemberId(long memberId) { this.memberId = memberId; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Salt{" +
                "memberId=" + memberId +
                ", salt='[PROTECTED]'" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
