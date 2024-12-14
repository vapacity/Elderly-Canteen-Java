package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.dto.dish.DishDto;
import com.javaee.elderlycanteen.entity.Dish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishDao {
    @Select("Select * from Dish where dishId = #{dishId}")
    public Dish getDishById(Integer dishId);

    @Select("Select * from Dish")
    public List<Dish> getAllDish();

    @Select("Select * from Dish where dishName = #{dishName}")
    public Dish getDishByName(String dishName);

    @Select("SELECT * FROM Dish WHERE dishName LIKE CONCAT('%', #{dishName}, '%')")
    public List<Dish> getDishByNameVague(String dishName);

    @Select("SELECT d.* " +
            "FROM Dish d " +
            "JOIN Category c ON d.cateId = c.cateId " +
            "WHERE d.dishName LIKE CONCAT('%', #{dishName}, '%') " +
            "AND c.cateName LIKE CONCAT('%', #{cateName}, '%')")
    public List<Dish> getDishesByNameAndCategoryVague(@Param("dishName") String dishName, @Param("cateName") String cateName);

    public DishDto getDishDetailsById(@Param("dishId") Integer dishId);

    @Select("Select * from Dish where cateId = #{cateId}")
    public Dish getDishByCateId(Integer cateId);

    @Select("Select * from Dish where cateId = #{cateId} and dishName like #{dishName}")
    public List<Dish> getDishByCateIdAndName(Integer cateId, String dishName);

    List<Dish> findAllByDishNameAndCategoryName(@Param("dishName") String dishName,
                                                @Param("categoryName") String categoryName);




    @Insert("Insert into Dish(dishName, price, cateId,imageUrl) values(#{dishName}, #{price}, #{cateId}, #{imageUrl})")
    @Options(useGeneratedKeys = true,keyProperty = "dishId")
    public Integer insertDish(Dish dish);

    @Update("Update Dish set dishName = #{dishName}, price = #{price}, cateId = #{cateId}, imageUrl = #{imageUrl} where dishId = #{dishId}")
    public Integer updateDish(Dish dish);

    @Delete("Delete from Dish where dishId = #{dishId}")
    public Integer deleteDish(Integer dishId);


}