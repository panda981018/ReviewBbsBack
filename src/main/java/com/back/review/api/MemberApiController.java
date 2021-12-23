package com.back.review.api;

import com.back.review.dto.MemberDto;
import com.back.review.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/getUser")
    public MemberDto getUser(@RequestParam String username) throws SQLException {
        return memberService.getByUsername(username);
    }

    @PostMapping("/signup")
    public Long signUp(@RequestBody MemberDto memberDto) {
        return memberService.signUp(memberDto);
    }
}
