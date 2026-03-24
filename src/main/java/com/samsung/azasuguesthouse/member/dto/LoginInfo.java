package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public class LoginInfo {
    @Schema(description = "아이디 (8~15자)", example = "admin123")
    private String loginId;

    @Schema(description = "비밀번호 (12~20자)", example = "password123456")
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
