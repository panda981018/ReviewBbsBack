package com.back.review.api;

import com.back.review.dto.MemberDto;
import com.back.review.dto.ReplyDto;
import com.back.review.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.back.review.conf.AppConfig.getClientIp;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reply")
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping("/add")
    public String createReply(HttpServletRequest request, @RequestBody HashMap<String, String> obj) throws Exception {
        HttpSession session = request.getSession();
        String bbsId = obj.get("bbsId");
        System.out.println(bbsId);
        ReplyDto replyDto = ReplyDto.builder()
                .contents(obj.get("contents"))
                .bbs(Long.parseLong(bbsId))
                .ipAddr(getClientIp(request))
                .build();
        replyService.createReply(session, replyDto);

        return "ok";
    }

    @DeleteMapping("/delete")
    public String deleteReply(@RequestBody HashMap<String, String> deleteData) {

        String bbsId = deleteData.get("bbsId");
        String replyId = deleteData.get("replyId");
        replyService.removeReply(Long.parseLong(replyId), Long.parseLong(bbsId));

        return "ok";
    }

    @PostMapping("/update")
    public void updateReply(@RequestBody HashMap<String, String> updateObj, HttpSession session) {
        MemberDto member = (MemberDto) session.getAttribute("memberInfo");
        replyService.updateReply(updateObj.get("contents"), Long.parseLong(updateObj.get("id")), member);
    }
}