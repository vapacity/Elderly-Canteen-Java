package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.RepositoryLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepositoryLogDao {

    // 根据主键查询单个对象
    @Select("SELECT * FROM RepositoryLog WHERE logId = #{logId}")
    RepositoryLog selectById(Integer logId);

    // 查询所有对象
    @Select("SELECT * FROM repository_log")
    List<RepositoryLog> selectAll();

    // 插入对象
    @Insert("INSERT INTO repository_log(actionType, entityType, entityId, oldValue, newValue, changedAt, changedBy) " +
            "VALUES(#{actionType}, #{entityType}, #{entityId}, #{oldValue}, #{newValue}, #{changedAt}, #{changedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    Integer insert(RepositoryLog repositoryLog);

    // 更新对象
    @Update("UPDATE repository_log SET " +
            "actionType = #{actionType}, " +
            "entityType = #{entityType}, " +
            "entityId = #{entityId}, " +
            "oldValue = #{oldValue}, " +
            "newValue = #{newValue}, " +
            "changedAt = #{changedAt}, " +
            "changedBy = #{changedBy} " +
            "WHERE logId = #{logId}")
    Integer update(RepositoryLog repositoryLog);

    // 根据主键删除对象
    @Delete("DELETE FROM repository_log WHERE logId = #{logId}")
    Integer deleteById(Integer logId);

}