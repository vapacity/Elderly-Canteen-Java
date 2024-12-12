package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Ingredient;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IngredientDao {

    // 基于主码id查询
    @Select("SELECT * FROM ingredient WHERE ingredientId = #{ingredientId}")
    Ingredient getIngredientById(Integer ingredientId);

    // 查询所有数据
    @Select("SELECT * FROM ingredient")
    List<Ingredient> getAllIngredients();

    // 插入数据，主码id自增
    @Insert("INSERT INTO ingredient (ingredientName) VALUES (#{ingredientName})")
    @Options(useGeneratedKeys = true, keyProperty = "ingredientId", keyColumn = "ingredientId")
    Integer insertIngredient(Ingredient ingredient);

    // 更新数据
    @Update("UPDATE ingredient SET ingredientName = #{ingredientName} WHERE ingredientId = #{ingredientId}")
    Integer updateIngredient(Ingredient ingredient);

    // 删除数据
    @Delete("DELETE FROM ingredient WHERE ingredientId = #{ingredientId}")
    Integer deleteIngredient(Integer ingredientId);
}
