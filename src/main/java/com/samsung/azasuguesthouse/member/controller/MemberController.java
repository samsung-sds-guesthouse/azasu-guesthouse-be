package com.samsung.azasuguesthouse.member.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.member.dto.ChangePwInfo;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.dto.SignupInfo;
import com.samsung.azasuguesthouse.member.dto.WithdrawInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@Tag(name = "Auth API", description = "회원 인증 API (로그인, 회원가입)")
@RestController
@RequestMapping("/api/v1/auth")
@Transactional(rollbackFor=Exception.class, noRollbackFor=InvalidPasswordException.class)
public class MemberController {
    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인 후 세션 쿠키(JSESSIONID)를 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginInfo info, HttpServletRequest request) {
        Member member = memberService.login(info);
        HttpSession session = request.getSession();
        session.setAttribute("member", member);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(
                        Map.of(
                                "name", HtmlUtils.htmlEscape(member.getName()),
                                "role", member.getRole()
                        )
                ));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 후 세션을 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@AuthInfo Member member, HttpServletRequest request) {
        memberService.logout(member);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signup(@RequestBody SignupInfo info) {
        memberService.signup(info);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @Operation(summary = "아이디 중복 체크", description = "제출한 아이디가 기존에 없던 아이디인지 확인합니다.")
    @GetMapping("/duplicate-id")
    public ResponseEntity<SuccessResponse> duplicateId(@RequestParam("login_id") String loginId) {
        memberService.duplicateId(loginId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @Operation(summary = "회원탈퇴", description = "로그인된 회원 정보를 삭제합니다.")
    @PostMapping("/withdraw")
    public ResponseEntity<SuccessResponse> withdraw(@AuthInfo Member member, @RequestBody WithdrawInfo info, HttpServletRequest request) {
        memberService.withdraw(member, info);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @Operation(summary = "마이페이지", description = "내 정보를 확인합니다.")
    @GetMapping("/my-info")
    public ResponseEntity<SuccessResponse> myInfo(@AuthInfo Member member) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(
                        Map.of(
                                "login_id", HtmlUtils.htmlEscape(member.getLoginId()),
                                "name", HtmlUtils.htmlEscape(member.getName()),
                                "phone", member.getPhone()
                        )
                ));
    }

    @Operation(summary = "비밀번호 변경", description = "로그인된 회원의 비밀번호를 변경합니다.")
    @PostMapping("/change-pw")
    public ResponseEntity<SuccessResponse> changePw(@AuthInfo Member member, @RequestBody ChangePwInfo info) {
        memberService.changePw(member, info);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }
}
