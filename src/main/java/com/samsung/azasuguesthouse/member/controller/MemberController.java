package com.samsung.azasuguesthouse.member.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.dto.SignupInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
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

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signup(@RequestBody SignupInfo info, HttpServletRequest request) {
        memberService.signup(info);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @Hidden
    @GetMapping("/test")
    public ResponseEntity<SuccessResponse> test(@AuthInfo Member member) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(Map.of("name", "홍길동")));
    }
}
