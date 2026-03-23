package com.samsung.azasuguesthouse.entity.member;

import java.time.LocalDateTime;

public class Salt {

    private long memberId;      // 회원 고유 번호 (PK이자 FK)
    private String salt;        // 암호화용 솔트 값
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 1. 기본 생성자 (MyBatis 필수)
    public Salt() {
    }

    // 2. 전체 필드 생성자
    public Salt(long memberId, String salt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.salt = salt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 3. Getter / Setter
    public long getMemberId() { return memberId; }
    public void setMemberId(long memberId) { this.memberId = memberId; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 4. toString() (보안상 salt 값은 로그에 남기지 않거나 마스킹할 수 있지만, 여기서는 포함합니다)
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
