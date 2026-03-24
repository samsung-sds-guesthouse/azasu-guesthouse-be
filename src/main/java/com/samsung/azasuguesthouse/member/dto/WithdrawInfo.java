package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원탈퇴 요청")
public class WithdrawInfo {
    @Schema(description = "비밀번호 (12~20자)", example = "password123456")
    private String password;

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
