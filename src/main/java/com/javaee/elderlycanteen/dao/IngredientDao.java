package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Ingredient;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IngredientDao {

    // 基于主码id查询
    @Select("SELECT * FROM Ingredient WHERE ingredientId = #{ingredientId}")
    Ingredient getIngredientById(Integer ingredientId);

    @Select("SELECT * FROM Ingredient WHERE ingredientName = #{ingredientName}")
    List<Ingredient> getIngredientByName(String ingredientName);
    // 查询所有数据
    @Select("SELECT * FROM Ingredient")
    List<Ingredient> getAllIngredients();

    // 插入数据，主码id自增
    @Insert("INSERT INTO Ingredient (ingredientName) VALUES (#{ingredientName})")
    @Options(useGeneratedKeys = true, keyProperty = "ingredientId", keyColumn = "ingredientId")
    Integer insertIngredient(Ingredient ingredient);

    // 更新数据
    @Update("UPDATE Ingredient SET ingredientName = #{ingredientName} WHERE ingredientId = #{ingredientId}")
    Integer updateIngredient(Ingredient ingredient);

    // 删除数据
    @Delete("DELETE FROM Ingredient WHERE ingredientId = #{ingredientId}")
    Integer deleteIngredient(Integer ingredientId);
}
