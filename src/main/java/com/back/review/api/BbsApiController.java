package com.back.review.api;

import com.back.review.dto.BbsDto;
import com.back.review.dto.HeartDto;
import com.back.review.service.BbsService;
import com.back.review.service.FavoriteService;
import com.back.review.service.HeartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bbs")
public class BbsApiController {

    private final BbsService bbsService;
    private final HeartService heartService;
    private final FavoriteService favoriteService;

    @GetMapping("/get/all")
    public HashMap<String, Object> getAllBbsList(@RequestParam String category, // 카테고리 번호
                                                 @RequestParam int perPage, // 한페이지당 보여줄 데이터의 개수
                                                 @RequestParam int page,
                                                 @RequestParam(required = false) String column,
                                                 @RequestParam(required = false) String searchType,// 검색 유형
                                                 @RequestParam(required = false) String keyword) throws Exception {
        PageRequest pageRequest =
                PageRequest.of(page-1, perPage, Sort.by(Sort.Direction.DESC, column));

        HashMap<String, Object> dataObj
                = bbsService.getBbsPagination(Long.parseLong(category), pageRequest, searchType, keyword);
        List<BbsDto> contents = (List<BbsDto>) dataObj.get("bbsDtoList");
        long totalCount = (long) dataObj.get("totalCount");

        /* 클라이언트에게 보낼 때 지켜야할 데이터 형식
        {
          "result": true,
          "data": {
            "contents": [],
            "pagination": {
              "page": 1,
              "totalCount": 100
            }
          }
        }
         */
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> dataMap = new HashMap<>();
        HashMap<String, Object> paginationMap = new HashMap<>();

        paginationMap.put("page", page);
        paginationMap.put("totalCount", totalCount);
        dataMap.put("contents", contents);
        dataMap.put("pagination", paginationMap);

        responseMap.put("result", true);
        responseMap.put("data", dataMap);

        return responseMap;
    }

    @GetMapping("/get/home")
    public List<BbsDto> getMemberHomeData(@RequestParam String perPage) {
        PageRequest pageRequest =
                PageRequest.of(0, Integer.parseInt(perPage), Sort.by(Sort.Direction.DESC, "bbsDate"));
        return bbsService.getHomeBbs(pageRequest);
    }

    @GetMapping("/view")
    public HashMap<String, Object> viewBbs(@RequestParam Long bbsId, @RequestParam Long memberId) throws Exception {
        HashMap<String, Object> dataMap = bbsService.getBbs(bbsId);
        HeartDto heartDto = heartService.findHeartObject(bbsId, memberId);

        BbsDto bbs = (BbsDto) dataMap.get("bbsDto");
        boolean favDto = favoriteService.findFavObject(bbs.getLatitude(), bbs.getLongitude(), memberId);

        dataMap.put("categoryId", ((BbsDto) dataMap.get("bbsDto")).getCategoryId() - 1);
        dataMap.put("heartObj", heartDto);
        dataMap.put("favObj", favDto);

        return dataMap;
    }

    @PostMapping("/update/views") // 조회수 업데이트 기능
    public void updateViews(@RequestBody HashMap<String, String> bbsIdObj) throws Exception {
        bbsService.updateViews(Long.parseLong(bbsIdObj.get("id")));
    }

    @DeleteMapping("/delete")
    public void deleteBbs(@RequestBody HashMap<String, Object> data) {

        if (data.containsKey("urls")) {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), (List<String>) data.get("urls"));
        } else {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), null);
        }
    }
}
