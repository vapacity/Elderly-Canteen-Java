package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.VolApplication;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VolApplicationDao {

    // 根据 applicationId 查询单个对象
    @Select("SELECT * FROM VolApplication WHERE applicationId = #{applicationId}")
    VolApplication selectByApplicationId(Integer applicationId);

    // 查询所有对象
    @Select("SELECT * FROM VolApplication")
    List<VolApplication> selectAll();

    // 插入对象
    @Insert("INSERT INTO VolApplication(accountId, selfStatement, applicationDate) " +
            "VALUES(#{volApplication.getAccountId()}, #{volApplication.getSelfStatement()}, #{volApplication.getApplicationDate()})")
    @Options(useGeneratedKeys = true, keyProperty = "applicationId")
    Integer insert(VolApplication volApplication);

    // 更新对象
    @Update("UPDATE VolApplication SET " +
            "accountId = #{volApplication.getAccountId()}, " +
            "selfStatement = #{volApplication.getSelfStatement()}, " +
            "applicationDate = #{volApplication.getApplicationDate()} " +
            "WHERE applicationId = #{volApplication.getApplicationId()}")
    Integer update(VolApplication volApplication);

    // 根据 applicationId 删除对象
    @Delete("DELETE FROM VolApplication WHERE applicationId = #{applicationId}")
    Integer deleteByApplicationId(Integer applicationId);

}