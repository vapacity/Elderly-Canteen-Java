package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Repository;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface RepositoryDao {

    // 根据主键查询单个对象
    @Select("SELECT * FROM Repository WHERE ingredientId = #{id}")
    Repository selectById(Integer id);

    @Select("SELECT r.*, i.ingredientName " +
            "FROM Repository r " +
            "JOIN Ingredient i ON r.ingredientId = i.ingredientId " +
            "WHERE i.ingredientName LIKE CONCAT('%', #{ingredientName}, '%')")
    List<Repository> searchAllReposByNameVague(@Param("ingredientName") String ingredientName);

    // 查询所有对象
    @Select("SELECT * FROM Repository")
    List<Repository> selectAll();

    // 插入对象，主键自增
    @Insert("INSERT INTO Repository(ingredientId, remainAmount, highConsumption, expirationTime) " +
            "VALUES(#{ingredientId},#{remainAmount}, #{highConsumption}, #{expirationTime})")
    Integer insert(Repository repository);

    // 更新对象
    @Update("UPDATE Repository SET " +
            "remainAmount = #{remainAmount}, " +
            "highConsumption = #{highConsumption} " +
            "WHERE ingredientId = #{ingredientId} and expirationTime = #{expirationTime}")
    Integer update(Repository repository);

    // 根据主键删除对象
    @Delete("DELETE FROM Repository WHERE ingredientId = #{id}")
    Integer deleteById(Integer id);

    @Select("Select * from Repository Where ingredientId = #{ingredientId} and expirationTime = #{oldExpiry}")
    Repository selectByIngredientIdAndExpiry(Integer ingredientId, Date oldExpiry);

    @Delete("DELETE FROM Repository WHERE ingredientId = #{ingredientId} and expirationTime = #{oldExpiry}")
    Integer deleteByIngredientIdAndExpiry(Integer ingredientId, Date oldExpiry);

    @Update("UPDATE Repository SET remainAmount = #{amount} WHERE ingredientId = #{ingredientId} and expirationTime = #{expiry}")
    void updateRemainAmount(Integer ingredientId, Date expiry, Integer amount);

    @Select("SELECT SUM(remainAmount) FROM Repository WHERE ingredientId = #{ingredientId}")
    Integer getRemainAmountByIngredientId(Integer ingredientId);


    @Select("Select * From Repository Where ingredientId = #{ingredientId} ORDER BY expirationTime ASC")
    List<Repository> selectByIdOrderByExpiry(Integer ingredientId);
}
