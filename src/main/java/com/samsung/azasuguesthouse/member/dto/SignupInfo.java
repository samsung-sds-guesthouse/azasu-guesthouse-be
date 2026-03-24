package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;

public class SignupInfo {
    private String loginId;
    private String password;
    private String name;
    private String phone;

    public String getLoginId() {
        return this.loginId;
    }
    public void setLoginId(String loginId) {
        if (loginId == null || loginId.length() < 8 || loginId.length() > 15) {
            throw new InvalidInputException("invalid_login_id");
        }
        this.loginId = loginId;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        if (password == null || password.length() < 12 || password.length() > 20) {
            throw new InvalidInputException("invalid_password");
        }
        this.password = password;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        if (name == null || name.isEmpty() || name.length() > 30) {
            throw new InvalidInputException("invalid_name");
        }
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        if (phone == null || !phone.matches("^010\\d{8}$")) {
            throw new InvalidInputException("invalid_phone");
        }
        this.phone = phone;
    }
}
