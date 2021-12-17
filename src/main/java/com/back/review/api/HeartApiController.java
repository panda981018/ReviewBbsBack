package com.back.review.api;

import com.back.review.dto.MemberDto;
import com.back.review.service.BbsService;
import com.back.review.service.HeartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/heart")
public class HeartApiController {

    private final HeartService heartService;
    private final BbsService bbsService;

    @PostMapping("/like")
    public HashMap<String, Integer> likeBbs(@RequestBody HashMap<String, String> likeObj, HttpSession session) throws Exception {
        String bid = likeObj.get("bbsId");
        String type = likeObj.get("type");
        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");

        int updatedLikeCnt;

        if (type.equals("like")) { // like일 경우
            heartService.plusHeartCount(Long.parseLong(bid), memberDto.getId());
        } else { // cancel일 경우
            heartService.minusHeartCount(Long.parseLong(bid), memberDto.getId());
        }
        updatedLikeCnt = bbsService.updateLikeCount(Long.parseLong(bid), type);

        HashMap<String, Integer> mapData = new HashMap<>();
        mapData.put("likeCnt", updatedLikeCnt);

        return mapData;
    }
}