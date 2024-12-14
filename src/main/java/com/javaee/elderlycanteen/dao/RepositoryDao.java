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
    @Insert("INSERT INTO Repository(remainAmount, highConsumption, expirationTime) " +
            "VALUES(#{repository.getRemainAmount()}, #{repository.getHighConsumption()}, #{repository.getExpirationTime()})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Repository repository);

    // 更新对象
    @Update("UPDATE Repository SET " +
            "remainAmount = #{repository.getRemainAmount()}, " +
            "highConsumption = #{repository.getHighConsumption()}, " +
            "expirationTime = #{repository.getExpirationTime()} " +
            "WHERE ingredientId = #{repository.getIngredientId()}")
    Integer update(Repository repository);

    // 根据主键删除对象
    @Delete("DELETE FROM Repository WHERE ingredientId = #{id}")
    Integer deleteById(Integer id);

    @Select("Select * from Repository Where ingredientId = #{ingredientId} and expirationTime = #{oldExpiry}")
    Repository selectByIngredientIdAndExpiry(Integer ingredientId, Date oldExpiry);
}
