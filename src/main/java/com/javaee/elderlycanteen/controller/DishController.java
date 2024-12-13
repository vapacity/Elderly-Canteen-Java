package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.dish.AllDishResponseDto;
import com.javaee.elderlycanteen.minio.MinioService;
import com.javaee.elderlycanteen.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/Dish")
public class DishController {

    private final DishService dishService;
    private final MinioService minioService;

    @Autowired
    public DishController(DishService dishService, MinioService minioService) {
        this.dishService = dishService;
        this.minioService = minioService;
    }

    // 在这里添加特定的API端点方法
    @GetMapping("/search")
    public AllDishResponseDto searchDish(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value="category",required = false) String category){
        return dishService.searchDish(name, category);
    }

//    @PostMapping("/addDish")
//
//    @PutMapping("/updateDish")
//
//    @DeleteMapping("/deleteDish/{dishId}")

}