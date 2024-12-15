package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Restock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RestockDao {

    // 根据 financeId 查询单个对象
    @Select("SELECT * FROM Restock WHERE financeId = #{financeId}")
    Restock selectByFinanceId(Integer financeId);

    //查询是否存在管理员参与过进货
    @Select("SELECT COUNT(*) FROM Restock WHERE administratorId = #{administratorId}")
    Restock getRestockById(Integer administratorId);

    // 查询所有对象
    @Select("SELECT * FROM Restock")
    List<Restock> selectAll();

    // 插入对象
//    @Insert("INSERT INTO Restock(ingredientId, administratorId, quantity, price) " +
//            "VALUES(#{ingredientId}, #{administratorId}, #{quantity}, #{price})")
//    @Options(useGeneratedKeys = true, keyProperty = "financeId")
//    Integer insert(Restock restock);

    // 更新对象
//    @Update("UPDATE Restock SET " +
//            "ingredientId = #{ingredientId}, " +
//            "administratorId = #{administratorId}, " +
//            "quantity = #{quantity}, " +
//            "price = #{price} " +
//            "WHERE financeId = #{financeId}")
//    Integer update(Restock restock);

    // 根据 financeId 删除对象
    @Delete("DELETE FROM Restock WHERE financeId = #{financeId}")
    Integer deleteByFinanceId(Integer financeId);


    // 插入对象
    @Insert("INSERT INTO Restock(financeId, ingredientId, administratorId, quantity, price) " +
            "VALUES(#{financeId}, #{ingredientId}, #{administratorId}, #{quantity}, #{price})")
    void insertRestock(Restock restock);
}
