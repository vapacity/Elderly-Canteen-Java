package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CategoryDao;
import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dao.FormulaDao;
import com.javaee.elderlycanteen.dao.IngredientDao;
import com.javaee.elderlycanteen.dto.dish.*;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.entity.Formula;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {

    private final DishDao dishDao;
    private final CategoryDao categoryDao;
    private final IngredientDao ingredientDao;
    private final FormulaDao formulaDao;

    @Autowired
    public DishService(DishDao dishDao, CategoryDao categoryDao, IngredientDao ingredientDao, FormulaDao formulaDao) {
        this.dishDao = dishDao;
        this.categoryDao = categoryDao;
        this.ingredientDao = ingredientDao;
        this.formulaDao = formulaDao;
    }

    public AllDishResponseDto searchDish(String name, String category) {
        AllDishResponseDto allDishResponseDto = new AllDishResponseDto();
        List<DishDto> dishDtoList = new ArrayList<DishDto>();
        List<Dish> dishes = new ArrayList<Dish>();

        if (name == null && category == null) {
            dishes = dishDao.getAllDish();
        } else {
            dishes = dishDao.getDishesByNameAndCategoryVague(name, category);
        }
        if (dishes.size() == 0) {
            throw new NotFoundException("Dishes not found");
        }
        for (Dish dish : dishes) {
            DishDto dishDto = new DishDto();
            dishDto = dishDao.getDishDetailsById(dish.getDishId());
            dishDtoList.add(dishDto);
        }
        allDishResponseDto.setResponse(dishDtoList);
        allDishResponseDto.setSuccess(true);
        allDishResponseDto.setMsg("Success");
        return allDishResponseDto;
    }

    public DishResponseDto addDish(DishRequestDto dishRequestDto) {

        // 检查dish是否存在
        if (dishDao.getDishByName(dishRequestDto.getName())!= null) {
            throw new InvalidInputException("Dish already exists");
        }

        if(!checkVadality(dishRequestDto))
            throw new ServiceException("Vadality check failed");
        // 插入菜品
        String dishName = dishRequestDto.getName();
        Integer dishCateId = dishRequestDto.getCateId();
        Double price = dishRequestDto.getPrice();

        Dish dish = new Dish(1,dishName,price,dishCateId,"null");
        dishDao.insertDish(dish);

        // 插入配方
        Integer dishId = dishDao.getDishByName(dishName).getDishId();
        for (FormulaDto formulaDto : dishRequestDto.getFormula()) {
            Integer ingredientId = formulaDto.getIngredientId();
            Integer amount = formulaDto.getAmount();
            Formula formula = new Formula(dishId, ingredientId, amount);
            formulaDao.insertFormula(formula);
        }

        //获得dishDto作为返回对象
        DishDto dishDto = dishDao.getDishDetailsById(dishId);
        DishResponseDto dishResponseDto = new DishResponseDto();
        dishResponseDto.setDish(dishDto);
        dishResponseDto.setSuccess(true);
        dishResponseDto.setMsg("Success");
        return dishResponseDto;
    }
    private Boolean checkVadality(DishRequestDto dishRequestDto){
        String dishName = dishRequestDto.getName();
        Integer dishCateId = dishRequestDto.getCateId();
        Double price = dishRequestDto.getPrice();
        Integer dishId = dishRequestDto.getDishId(); // 假设DishRequestDto中包含dishId字段，用于指定更新的菜品

        // 检查category是否存在
        if (categoryDao.findById(dishCateId) == null) {
            throw new NotFoundException("Category not found");
        }

        // 检查ingredienId是否存在
        for (FormulaDto formulaDto : dishRequestDto.getFormula()) {
            Integer ingredientId = formulaDto.getIngredientId();
            Integer amount = formulaDto.getAmount();
            if (ingredientId == null || amount == null) {
                throw new ServiceException("Invalid formula");
            }

            if (ingredientDao.getIngredientById(ingredientId) == null) {
                throw new NotFoundException("Ingredient not found");
            }
        }
        return true;
    }

    public DishResponseDto updateDish(DishRequestDto dishRequestDto) {
        if(!checkVadality(dishRequestDto))
            throw new ServiceException("Vadality check failed");

        String dishName = dishRequestDto.getName();
        Integer dishCateId = dishRequestDto.getCateId();
        Double price = dishRequestDto.getPrice();
        Integer dishId = dishRequestDto.getDishId(); // 假设DishRequestDto中包含dishId字段，用于指定更新的菜品

        // 检查菜品是否存在
        Dish existingDish = dishDao.getDishById(dishId);

        if (existingDish == null) {
            throw new NotFoundException("Dish not found");
        }

        // 更新菜品信息
        existingDish.setDishName(dishName);
        existingDish.setPrice(price);

        dishDao.updateDish(existingDish);

        // 更新配方
        // 首先删除原有的配方（如果有的话）
        formulaDao.deleteFormulaByDishId(dishId);

        // 插入新的配方
        for (FormulaDto formulaDto : dishRequestDto.getFormula()) {
            Integer ingredientId = formulaDto.getIngredientId();
            Integer amount = formulaDto.getAmount();
            Formula formula = new Formula(dishId, ingredientId, amount);
            formulaDao.insertFormula(formula);
        }

        // 获取更新后的dishDto作为返回对象
        DishDto updatedDishDto = dishDao.getDishDetailsById(dishId);
        DishResponseDto dishResponseDto = new DishResponseDto();
        dishResponseDto.setDish(updatedDishDto);
        dishResponseDto.setSuccess(true);
        dishResponseDto.setMsg("Update successful");

        return dishResponseDto;
    }

    public DishResponseDto deleteDish(Integer dishId) {
        // 检查菜品是否存在
        Dish existingDish = dishDao.getDishById(dishId);
        if (existingDish == null) {
            throw new NotFoundException("Dish not found");
        }

        // 删除菜品配方
        formulaDao.deleteFormulaByDishId(dishId);

        // 删除菜品
        dishDao.deleteDish(dishId);

        // 返回响应
        DishResponseDto dishResponseDto = new DishResponseDto();
        dishResponseDto.setSuccess(true);
        dishResponseDto.setMsg("Dish deleted successfully");

        return dishResponseDto;
    }

    public void updateDishImage(Integer dishId, String fileUrl) {
        // 1. 获取菜品信息
        Dish existingDish = dishDao.getDishById(dishId);

        // 2. 验证菜品是否存在
        if (existingDish == null) {
            throw new NotFoundException("Dish not found with id: " + dishId);
        }

        // 3. 更新菜品图片 URL
        existingDish.setImageUrl(fileUrl);  // 假设菜品类有一个 imageUrl 字段来保存图片 URL

        // 4. 保存更新后的菜品信息
        dishDao.updateDish(existingDish);
    }

}

