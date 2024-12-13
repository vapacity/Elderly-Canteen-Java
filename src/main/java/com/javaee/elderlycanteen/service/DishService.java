package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CategoryDao;
import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dto.dish.AllDishResponseDto;
import com.javaee.elderlycanteen.dto.dish.DishDto;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {

    private final DishDao dishDao;
    private final CategoryDao categoryDao;
    @Autowired
    public DishService(DishDao dishDao, CategoryDao categoryDao) {
        this.dishDao = dishDao;
        this.categoryDao = categoryDao;
    }

    public AllDishResponseDto searchDish(String name, String category) {
        AllDishResponseDto allDishResponseDto = new AllDishResponseDto();
        List<DishDto> dishDtoList = new ArrayList<DishDto>();
        List<Dish> dishes = new ArrayList<Dish>();
        if(name == null && category == null){
            dishes = dishDao.getAllDish();
        }
        else{
            dishes = dishDao.getDishesByNameAndCategoryVague(name, category);
        }
        if(dishes.size() == 0){
            throw new NotFoundException("Dishes not found");
        }
        for(Dish dish : dishes){
            DishDto dishDto = new DishDto();
            dishDto = dishDao.getDishDetailsById(dish.getDishId());
            dishDtoList.add(dishDto);
        }
        allDishResponseDto.setResponse(dishDtoList);
        allDishResponseDto.setSuccess(true);
        allDishResponseDto.setMsg("Success");
        return allDishResponseDto;
    }

}