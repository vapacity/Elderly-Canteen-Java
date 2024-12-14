package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Formula;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FormulaDao {

    // 查询基于dishId和ingredientId的记录
    @Select("SELECT * FROM Formula WHERE dishId = #{dishId} AND ingredientId = #{ingredientId}")
    Formula getFormulaById(int dishId, int ingredientId);

    @Select("Select * from Formula where dishId = #{dishId}")
    List<Formula> getFormulasById(int dishId);

    // 查询所有的记录
    @Select("SELECT * FROM Formula")
    List<Formula> getAllFormulas();

    // 插入一条记录，主码id自增
    @Insert("INSERT INTO Formula(dishId, ingredientId, amount) VALUES (#{dishId}, #{ingredientId}, #{amount})")
    Integer insertFormula(Formula formula);

    // 更新记录
    @Update("UPDATE Formula SET amount = #{amount} WHERE dishId = #{dishId} AND ingredientId = #{ingredientId}")
    Integer updateFormula(Formula formula);

    // 删除记录
    @Delete("DELETE FROM Formula WHERE dishId = #{dishId} AND ingredientId = #{ingredientId}")
    Integer deleteFormula(int dishId, int ingredientId);

    @Delete("Delete from Formula where dishId = #{dishId}")
    Integer deleteFormulaByDishId(int dishId);
}
