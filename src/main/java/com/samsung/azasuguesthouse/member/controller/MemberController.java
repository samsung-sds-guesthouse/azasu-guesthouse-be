package com.samsung.azasuguesthouse.member.controller;

import com.samsung.azasuguesthouse.common.auth.AuthInfo;
import com.samsung.azasuguesthouse.common.response.SuccessResponse;
import com.samsung.azasuguesthouse.entity.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class MemberController {
//    @Autowired
//    MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("member", new Member());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse());
    }

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse> test(@AuthInfo Member member) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(String.valueOf(member.getId())));
    }
}
