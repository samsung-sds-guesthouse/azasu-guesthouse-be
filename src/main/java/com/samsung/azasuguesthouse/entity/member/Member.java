package com.samsung.azasuguesthouse.entity.member;

import com.samsung.azasuguesthouse.member.dto.SignupInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidInputException;

import java.time.LocalDateTime;

public class Member {

    private long id;
    private String loginId;
    private String password;
    private String name;
    private String phone;
    private Role role;
    private UserStatus status;
    private int tryCount;
    private LocalDateTime lastTryAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Member() {}

    public Member (SignupInfo info) {
        this.loginId = info.getLoginId();
        this.name = info.getName();
        this.phone = info.getPhone();
    }

    public long getId() { return id; }
    public void setId(long id) {
        if (id < 1) {
            throw new InvalidInputException("invalid_id");
        }
        this.id = id;
    }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) {
        if (loginId == null || loginId.length() < 8 || loginId.length() > 15) {
            throw new InvalidInputException("invalid_login_id");
        }
        this.loginId = loginId;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidInputException("invalid_password");
        }
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("invalid_name");
        }
        this.name = name;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        if (phone == null || !phone.matches("^010\\d{8}$")) {
            throw new InvalidInputException("invalid_phone");
        }
        this.phone = phone;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) {
        if (role == null) {
            throw new InvalidInputException("invalid_role");
        }
        this.role = role;
    }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) {
        if (status == null) {
            throw new InvalidInputException("invalid_status");
        }
        this.status = status;
    }

    public int getTryCount() {
        return tryCount;
    }
    public void setTryCount(int tryCount) {
        if (tryCount < 0) {
            throw new InvalidInputException("invalid_try_count");
        }
        this.tryCount = tryCount;
    }

    public LocalDateTime getLastTryAt() {
        return lastTryAt;
    }
    public void setLastTryAt(LocalDateTime lastTryAt) {
        this.lastTryAt = lastTryAt;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new InvalidInputException("invalid_created_at");
        }
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            throw new InvalidInputException("invalid_updated_at");
        }
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", tryCount=" + tryCount +
                ", lastTryAt=" + lastTryAt +
                ", createdAt=" + createdAt +
                '}';
    }
}