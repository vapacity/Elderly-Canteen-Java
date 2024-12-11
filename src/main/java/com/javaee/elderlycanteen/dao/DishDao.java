package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Dish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishDao {
    @Select("Select * from dish where dish_id = #{dishId}")
    public Dish getDishById(int dishId);

    @Select("Select * from dish")
    public List<Dish> getAllDish();

    @Select("Select * from dish where dish_name = #{dishName}")
    public Dish getDishByName(String dishName);

    @Select("Select * from dish where dish_type = #{dishType}")
    public List<Dish> getDishByType(String dishType);

    @Insert("Insert into dish(dish_name, price, cateId,imageUrl) values(#{dishName}, #{price}, #{cateId}), #{imageUrl})")
    public void insertDish(Dish dish);
}