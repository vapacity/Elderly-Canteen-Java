package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Senior;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SeniorDao {

    // 根据 accountId 查询单个对象
    @Select("SELECT * FROM Senior WHERE accountId = #{accountId}")
    Senior selectByAccountId(Integer accountId);

    // 查询所有对象
    @Select("SELECT * FROM Senior")
    List<Senior> selectAll();

    // 插入对象
    @Insert("INSERT INTO Senior(familyNum, subsidy) " +
            "VALUES(#{senior.getFamilyNum()}, #{senior.getSubsidy()})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    Integer insert(Senior senior);

    // 更新对象
    @Update("UPDATE Senior SET " +
            "familyNum = #{senior.getFamilyNum()}, " +
            "subsidy = #{senior.getSubsidy()} " +
            "WHERE accountId = #{senior.getAccountId()}")
    Integer update(Senior senior);

    // 根据 accountId 删除对象
    @Delete("DELETE FROM Senior WHERE accountId = #{accountId}")
    Integer deleteByAccountId(Integer accountId);
}