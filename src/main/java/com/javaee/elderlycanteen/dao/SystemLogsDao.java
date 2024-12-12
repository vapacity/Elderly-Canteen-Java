package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.SystemLogs;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SystemLogsDao {

    // 根据 logId 查询单个对象
    @Select("SELECT * FROM system_logs WHERE logId = #{logId}")
    SystemLogs selectByLogId(Integer logId);

    // 查询所有对象
    @Select("SELECT * FROM system_logs")
    List<SystemLogs> selectAll();

    // 插入对象
    @Insert("INSERT INTO system_logs(logLevel, message, createdAt) " +
            "VALUES(#{logLevel}, #{message}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    Integer insert(SystemLogs systemLogs);

    // 更新对象
    @Update("UPDATE system_logs SET " +
            "logLevel = #{logLevel}, " +
            "message = #{message}, " +
            "createdAt = #{createdAt} " +
            "WHERE logId = #{logId}")
    Integer update(SystemLogs systemLogs);

    // 根据 logId 删除对象
    @Delete("DELETE FROM system_logs WHERE logId = #{logId}")
    Integer deleteByLogId(Integer logId);

}