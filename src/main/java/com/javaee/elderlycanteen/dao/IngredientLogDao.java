package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.IngredientLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IngredientLogDao {

    // 基于主码id查询
    @Select("SELECT * FROM ingredient_log WHERE logId = #{logId}")
    IngredientLog getIngredientLogById(int logId);

    // 查询所有数据
    @Select("SELECT * FROM ingredient_log")
    List<IngredientLog> getAllIngredientLogs();

    // 插入数据，主码id自增
    @Insert("INSERT INTO ingredient_log (actionType, ingredientId, remainAmountOld, remainAmountNew, expirationTimeOld, expirationTimeNew, changeReason, changedBy, changedAt) " +
            "VALUES (#{actionType}, #{ingredientId}, #{remainAmountOld}, #{remainAmountNew}, #{expirationTimeOld}, #{expirationTimeNew}, #{changeReason}, #{changedBy}, #{changedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "logId", keyColumn = "logId")
    void insertIngredientLog(IngredientLog ingredientLog);

    // 更新数据
    @Update("UPDATE ingredient_log SET actionType = #{actionType}, ingredientId = #{ingredientId}, remainAmountOld = #{remainAmountOld}, remainAmountNew = #{remainAmountNew}, " +
            "expirationTimeOld = #{expirationTimeOld}, expirationTimeNew = #{expirationTimeNew}, changeReason = #{changeReason}, changedBy = #{changedBy}, changedAt = #{changedAt} " +
            "WHERE logId = #{logId}")
    void updateIngredientLog(IngredientLog ingredientLog);

    // 删除数据
    @Delete("DELETE FROM ingredient_log WHERE logId = #{logId}")
    void deleteIngredientLog(int logId);
}
