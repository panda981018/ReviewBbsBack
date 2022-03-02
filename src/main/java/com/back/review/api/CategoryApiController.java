package com.back.review.api;

import com.back.review.dto.CategoryDto;
import com.back.review.dto.MemberDto;
import com.back.review.service.CategoryService;
import com.back.review.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/get/all")
    public List<CategoryDto> getCategoryList() {
        return categoryService.getAllCategories();
    }
}
