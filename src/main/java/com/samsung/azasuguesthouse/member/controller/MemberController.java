package com.samsung.azasuguesthouse.member.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Transactional(rollbackFor=Exception.class)
public class MemberController {
    MemberService memberService = new MemberService();

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(LoginInfo info, HttpServletRequest request) {
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

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse> test(@AuthInfo Member member) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(Map.of("name", "홍길동")));
    }
}
