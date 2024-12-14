package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.SystemLogs;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface SystemLogsDao {

    // 根据 logId 查询单个对象
    @Select("SELECT * FROM SystemLogs WHERE logId = #{logId}")
    SystemLogs selectByLogId(Integer logId);

    // 查询所有对象
    @Select("SELECT * FROM SystemLogs")
    List<SystemLogs> selectAll();

    // 插入对象
    @Insert("INSERT INTO SystemLogs(logLevel, message, createdAt) " +
            "VALUES(#{logLevel}, #{message}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    Integer insert(String logLevel, String message, Date createdAt);

    // 更新对象
    @Update("UPDATE SystemLogs SET " +
            "logLevel = #{logLevel}, " +
            "message = #{message}, " +
            "createdAt = #{createdAt} " +
            "WHERE logId = #{logId}")
    Integer update(SystemLogs systemLogs);

    // 根据 logId 删除对象
    @Delete("DELETE FROM SystemLogs WHERE logId = #{logId}")
    Integer deleteByLogId(Integer logId);

}