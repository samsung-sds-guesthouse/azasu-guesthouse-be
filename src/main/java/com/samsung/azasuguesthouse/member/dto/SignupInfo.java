package com.samsung.azasuguesthouse.member.dto;

import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청")
public class SignupInfo {
    @Schema(description = "아이디 (8~15자)", example = "newuser1")
    private String loginId;

    @Schema(description = "비밀번호 (12~20자)", example = "password123456")
    private String password;

    @Schema(description = "이름 (최대 30자)", example = "홍길동")
    private String name;

    @Schema(description = "휴대폰 번호 (010으로 시작하는 11자리)", example = "01012345678")
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
