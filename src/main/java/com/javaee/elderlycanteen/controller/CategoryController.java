package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.category.AllCateResponseDto;
import com.javaee.elderlycanteen.dto.category.CateRequestDto;
import com.javaee.elderlycanteen.dto.category.CateResponseDto;
import com.javaee.elderlycanteen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
@Transactional
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/search")
    public AllCateResponseDto getCate(@RequestParam(required = false) String name) {
        return categoryService.searchCategories(name);
    }
    // 在这里添加特定的API端点方
    @PostMapping("/add")
    public CateResponseDto addCategory(@RequestBody CateRequestDto dto) {
        return categoryService.addCategory(dto);
    }

    @PutMapping("/update")
    public CateResponseDto updateCategory(@RequestBody CateRequestDto dto) {
        return categoryService.updateCategory(dto);
    }

    @DeleteMapping("/delete/{id}")
    public CateResponseDto deleteCategory(@PathVariable("id") int id) {
        return categoryService.deleteCategory(id);
    }
}