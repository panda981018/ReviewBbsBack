package com.back.review.api;

import com.back.review.dto.TestRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {

    // PathVariable 방식
    @GetMapping("/api/hello/{msg}")
    public ResponseEntity<String> testHello(@PathVariable String msg) throws Exception{
        String responseMsg = msg + " Return SUCCESS!!";
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    // url에 담긴 query들을 직접 받기
    @GetMapping("/api/hello")
    public ResponseEntity<String> testHello2(@RequestParam String firstData, @RequestParam String secondData) throws Exception{
        String responseMsg = "firstData = " + firstData + ". secondData = " + secondData + " Return SUCCESS!!";
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    // dto 형식으로 자동 매핑되도록 만들기
    @GetMapping("/api/hello2")
    public ResponseEntity<String> testMap(TestRequestDto testRequestDto) {
        String responseMsg = "id = " + testRequestDto.getId() + ", msg = " + testRequestDto.getMsg();
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
