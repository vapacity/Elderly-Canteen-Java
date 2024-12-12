package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Dish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishDao {
    @Select("Select * from dish where dish_id = #{dishId}")
    public Dish getDishById(int dishId);

    @Select("Select * from dish")
    public List<Dish> getAllDish();

    @Select("Select * from dish where dish_name = #{dishName}")
    public Dish getDishByName(String dishName);

    @Select("Select * from dish where cateId = #{cateId}")
    public Dish getDishByCateId(int cateId);

    @Insert("Insert into dish(dish_name, price, cateId,imageUrl) values(#{dishName}, #{price}, #{cateId}), #{imageUrl})")
    @Options(useGeneratedKeys = true,keyProperty = "dishId")
    public Integer insertDish(Dish dish);

    @Update("Update dish set dish_name = #{dishName}, price = #{price}, cateId = #{cateId}, imageUrl = #{imageUrl} where dish_id = #{dishId}")
    public Integer updateDish(Dish dish);

    @Delete("Delete from dish where dish_id = #{dishId}")
    public Integer deleteDish(int dishId);


}