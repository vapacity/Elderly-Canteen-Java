package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.IngredientDao;
import com.javaee.elderlycanteen.dto.ingredient.AllIngreResponseDto;
import com.javaee.elderlycanteen.dto.ingredient.IngreRequestDto;
import com.javaee.elderlycanteen.dto.ingredient.IngreResponseDto;
import com.javaee.elderlycanteen.entity.Ingredient;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientDao ingredientDao;

    @Autowired
    public IngredientService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    // 在这里添加特定的业务逻辑方法
    @Transactional(readOnly = true)
    public AllIngreResponseDto getRepo(String name){
        AllIngreResponseDto response = new AllIngreResponseDto();
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        List<IngreRequestDto> ingreRequestDtos = new ArrayList<IngreRequestDto>();

        // 获得ingredient列表
        // 如果name为空，则获得所有ingredient列表
        // 如果name不为空，则获得name对应的ingredient列表
        if(name == null || name.isEmpty()){
            ingredients = ingredientDao.getAllIngredients();
        }
        else{
            ingredients = ingredientDao.getIngredientByName(name);
        }

        if(ingredients == null){
            throw new NotFoundException("Ingredient not found");
        }

        // 将ingredient列表转换为IngreRequestDto列表
        // 并将IngreRequestDto列表添加到response中
        for(Ingredient ingredient : ingredients){
            IngreRequestDto ingreRequestDto = new IngreRequestDto();
            ingreRequestDto.setIngredientId(ingredient.getIngredientId());
            ingreRequestDto.setIngredientName(ingredient.getIngredientName());
            ingreRequestDtos.add(ingreRequestDto);
        }
        response.setIngredients(ingreRequestDtos);
        response.setMsg("success");
        response.setSuccess(true);
        return response;
    }

    public IngreResponseDto addIngredient(IngreRequestDto ingreRequestDto) {
        Ingredient ingredient = new Ingredient();
        IngreResponseDto response = new IngreResponseDto();

        List<Ingredient> ingredients = ingredientDao.getIngredientByName(ingreRequestDto.getIngredientName());
        if(ingredients.size() > 0) {
            response.setMsg("Ingredient already exists");
            response.setSuccess(false);
            return response;

        }

        ingredient.setIngredientName(ingreRequestDto.getIngredientName());
        ingredientDao.insertIngredient(ingredient);

        response.setMsg("success");
        response.setSuccess(true);
        response.setIngredientId(ingredient.getIngredientId());
        response.setIngredientName(ingredient.getIngredientName());
        return response;
    }

    public IngreResponseDto updateIngredient(IngreRequestDto ingreRequestDto) {
        Ingredient ingredient = ingredientDao.getIngredientById(ingreRequestDto.getIngredientId());
        if(ingredient == null) {
            throw new NotFoundException("Ingredient not found");
        }

        ingredient.setIngredientName(ingreRequestDto.getIngredientName());
        ingredientDao.updateIngredient(ingredient);

        IngreResponseDto response = new IngreResponseDto();
        response.setMsg("success");
        response.setSuccess(true);
        response.setIngredientId(ingredient.getIngredientId());
        response.setIngredientName(ingredient.getIngredientName());
        return response;
    }

    public IngreResponseDto deleteIngredient(Integer id) {
        Ingredient ingredient = ingredientDao.getIngredientById(id);
        if(ingredient == null) {
            throw new NotFoundException("Ingredient not found");
        }

        ingredientDao.deleteIngredient(id);

        IngreResponseDto response = new IngreResponseDto();
        response.setMsg("success");
        response.setSuccess(true);
        response.setIngredientId(ingredient.getIngredientId());
        response.setIngredientName(ingredient.getIngredientName());
        return response;
    }
}