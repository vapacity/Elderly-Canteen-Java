package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.ResponseDto;
import com.javaee.elderlycanteen.dto.dish.AllDishResponseDto;
import com.javaee.elderlycanteen.dto.dish.DishRequestDto;
import com.javaee.elderlycanteen.dto.dish.DishResponseDto;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.minio.MinioService;
import com.javaee.elderlycanteen.service.DishService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/addDish")
    public DishResponseDto addDish(@RequestBody DishRequestDto dishRequestDto) {
        return dishService.addDish(dishRequestDto);
    }

    @PutMapping("/updateDish")
    public DishResponseDto updateDish(@RequestBody DishRequestDto dishRequestDto) {
        return dishService.updateDish(dishRequestDto);
    }

    @DeleteMapping("/deleteDish/{dishId}")
    public DishResponseDto deleteDish(@PathVariable("dishId") int dishId) {
        return dishService.deleteDish(dishId);
    }
    @PostMapping("/uploadDishImage")
    public ResponseDto<String> uploadDishImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dishId") Integer dishId) throws Exception {

        if (file.isEmpty()) {
            throw new ServiceException("No file uploaded.");
        }

        String fileName = file.getOriginalFilename();
        String fileUrl = minioService.uploadFile(fileName, file);
        dishService.updateDishImage(dishId, fileUrl);

        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setData(fileUrl);
        responseDto.setSuccess(true);
        responseDto.setMessage("Image uploaded successfully");
        return responseDto;
    }

}