package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.service.BbsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/bbs")
public class BbsApiController {

    private final BbsService bbsService;

    @ResponseBody
    @GetMapping("/get")
    public HashMap<String, Object> getAllBbsList(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) int perPage, // 한페이지당 보여줄 데이터의 개수
                                                 @RequestParam(required = false) int page,
                                                 @RequestParam(required = false) String column,
                                                 HttpServletRequest request) {
        System.out.println("post.js -> BbsApiController queryString: " + request.getQueryString());
        PageRequest pageRequest;
        if (column != null) {
            pageRequest =
                    PageRequest.of(page-1, perPage, Sort.by(Sort.Direction.DESC, column));
        } else {
            pageRequest =
                    PageRequest.of(page-1, perPage, Sort.by(Sort.Direction.DESC, "id"));
        }
        // service에서 page totalCount, page에 맞는 데이터들,
        HashMap<String, Object> dataObj = bbsService.findAll(Long.parseLong(category), pageRequest);
        List<BbsDto> contents = (List<BbsDto>) dataObj.get("bbsDtoList");
        Long totalCount = (Long) dataObj.get("totalCount");

        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> dataMap = new HashMap<>();
        HashMap<String, Object> paginationMap = new HashMap<>();

        paginationMap.put("page", page);
        paginationMap.put("totalCount", totalCount.intValue());
        dataMap.put("contents", contents);
        dataMap.put("pagination", paginationMap);

        responseMap.put("result", true);
        responseMap.put("data", dataMap);

        return responseMap;
    }

    @ResponseBody
    @PostMapping("/update/views") // 조회수 업데이트 기능
    public void updateViews(@RequestBody HashMap<String, String> bbsIdObj) {
        bbsService.updateViews(Long.parseLong(bbsIdObj.get("id")));
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public void deleteBbs(@RequestBody HashMap<String, Object> data) {

        if (data.containsKey("urls")) {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), (List<String>) data.get("urls"));
        } else {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), null);
        }
    }
}
