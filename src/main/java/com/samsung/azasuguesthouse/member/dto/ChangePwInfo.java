package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 변경 요청")
public class ChangePwInfo {
    @Schema(description = "기존 비밀번호 (12~20자)", example = "password123456")
    private String oldPassword;
    @Schema(description = "새로운 비밀번호 (12~20자)", example = "password123456")
    private String newPassword;

    public String getOldPassword() {
        return this.oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        if (oldPassword == null || oldPassword.length() < 12 || oldPassword.length() > 20) {
            throw new InvalidInputException("invalid_password");
        }
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }
    public void setNewPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 12 || newPassword.length() > 20) {
            throw new InvalidInputException("invalid_password");
        }
        this.newPassword = newPassword;
    }
}
