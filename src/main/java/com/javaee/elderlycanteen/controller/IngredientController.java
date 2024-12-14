package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.annotation.CheckAccountIdentity;
import com.javaee.elderlycanteen.dto.ingredient.AllIngreResponseDto;
import com.javaee.elderlycanteen.dto.ingredient.IngreRequestDto;
import com.javaee.elderlycanteen.dto.ingredient.IngreResponseDto;
import com.javaee.elderlycanteen.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ingredients")
//@CheckAccountIdentity(identity = "admin")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // 在这里添加特定的API端点方法
    @GetMapping("/search")
    public AllIngreResponseDto searchIngredients(@RequestParam(value = "name", required = false) String name) {
        try{
            return ingredientService.getRepo(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 添加一个添加食材的API端点方法
    @PostMapping("/add")
    public IngreResponseDto addIngredient(@RequestBody IngreRequestDto ingreRequestDto) {
        return ingredientService.addIngredient(ingreRequestDto);

    }

    @PutMapping("/update")
    public IngreResponseDto updateIngredient(@RequestBody IngreRequestDto ingreRequestDto) {
        return ingredientService.updateIngredient(ingreRequestDto);
    }

    @DeleteMapping("/delete/{id}")
    public IngreResponseDto deleteIngredient(@PathVariable Integer id) {
        return ingredientService.deleteIngredient(id);
    }




}