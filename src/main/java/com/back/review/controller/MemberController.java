package com.back.review.controller;

import com.back.review.dto.MemberDto;
import com.back.review.enumclass.Role;
import com.back.review.service.CategoryService;
import com.back.review.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController { // member

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/getUser")
    public MemberDto getUser(@RequestParam String username) throws SQLException {
        return memberService.getByUsername(username);
    }

    @GetMapping("/home")
    public String memberHome(HttpSession session) {
        if (session.getAttribute("categoryList") != null) {
            session.setAttribute("categoryList", categoryService.getAllCategories());
        } else {
            session.removeAttribute("categoryList");
        }
        return "home/member-home";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(HttpSession session, Model model) throws SQLException { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.

        model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
        return "my-Info";
    }

    // 내정보에서 update 클릭했을 때
//    @PostMapping("/info")
//    public String postMyInfo(HttpSession session, MemberDto memberDto){
//
//        memberService.updateMember(session, memberDto);
//        MemberDto oldDto = (MemberDto) session.getAttribute("memberInfo");
//        if (oldDto.getRole().equals(Role.ADMIN.getTitle())) {
//            return "redirect:/admin/home";
//        } else if (oldDto.getRole().equals(Role.MEMBER.getTitle())) {
//            return "redirect:/member/home";
//        } else {
//            return "redirect:/";
//        }
//    }
}
