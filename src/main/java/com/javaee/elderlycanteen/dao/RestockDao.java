package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Restock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RestockDao {

    // 根据 financeId 查询单个对象
    @Select("SELECT * FROM Restock WHERE financeId = #{financeId}")
    Restock selectByFinanceId(Integer financeId);

    // 查询所有对象
    @Select("SELECT * FROM Restock")
    List<Restock> selectAll();

    // 插入对象
    @Insert("INSERT INTO Restock(ingredientId, administratorId, quantity, price) " +
            "VALUES(#{restock.getIngredientId()}, #{restock.getAdministratorId()}, #{restock.getQuantity()}, #{restock.getPrice()})")
    @Options(useGeneratedKeys = true, keyProperty = "financeId")
    Integer insert(Restock restock);

    // 更新对象
    @Update("UPDATE Restock SET " +
            "ingredientId = #{restock.getIngredientId()}, " +
            "administratorId = #{restock.getAdministratorId()}, " +
            "quantity = #{restock.getQuantity()}, " +
            "price = #{restock.getPrice()} " +
            "WHERE financeId = #{restock.getFinanceId()}")
    Integer update(Restock restock);

    // 根据 financeId 删除对象
    @Delete("DELETE FROM Restock WHERE financeId = #{financeId}")
    Integer deleteByFinanceId(Integer financeId);
}