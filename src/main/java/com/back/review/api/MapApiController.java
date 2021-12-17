package com.back.review.api;

import com.back.review.dto.BbsDto;
import com.back.review.dto.MemberDto;
import com.back.review.service.MapService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/map")
public class MapApiController {

    private final MapService mapService;

    @GetMapping("/getMarkers")
    public HashMap<String, Object> sendMarkerInfo(HttpServletRequest request, @RequestParam int page) {
        HttpSession session = request.getSession();
        PageRequest pageRequest = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.ASC, "id"));

        HashMap<String, Object> result
                = mapService.getPlaceInfo((MemberDto) session.getAttribute("memberInfo"), pageRequest);
        return result;
    }

    @GetMapping("/bbsList")
    public HashMap<String, Object> sendBbsList(@RequestParam double lat, @RequestParam double lng) {
        List<BbsDto> bbsList = mapService.getBbsList(lat, lng);

        HashMap<String, Object> map = new HashMap<>();
        map.put("result", bbsList);

        return map;
    }
}
