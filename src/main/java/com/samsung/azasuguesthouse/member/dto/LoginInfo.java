package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;

public class LoginInfo {
    private String loginId;
    private String password;

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
}
