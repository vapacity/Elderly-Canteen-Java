package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.ActionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ActionLogDao {
    @Select("SELECT * FROM actionlog")
    List<ActionLog> getAllActionLogs();

    @Select("SELECT * FROM actionlog WHERE accountId = #{accountId}")
    List<ActionLog> getActionLogsByAccountId(@Param("accountId") String accountId);

    @Select("SELECT * FROM actionlog WHERE actionType = #{actionType}")
    List<ActionLog> getActionLogsByActionType(@Param("actionType") String actionType);

    @Select("SELECT * FROM actionlog WHERE actionTime >= #{startTime} AND actionTime <= #{endTime}")
    List<ActionLog> getActionLogsByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT * FROM actionlog WHERE entityType = #{entityType} AND entityId = #{entityId}")
    ActionLog getActionLogById(@Param("entityType") String entityType, @Param("entityId") Integer entityId);

    @Insert("INSERT INTO actionlog (accountId, actionType, entityType, entityId, oldValue, newValue, actionTime) VALUES (#{accountId}, #{actionType}, #{entityType}, #{entityId}, #{oldValue}, #{newValue}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    Integer addActionLog(ActionLog actionLog);

    @Update("UPDATE actionlog SET oldValue = #{oldValue}, newValue = #{newValue}, actionTime = NOW() WHERE logId = #{logId}")
    Integer updateActionLog(ActionLog actionLog);

    @Delete("DELETE FROM actionlog WHERE logId = #{logId}")
    Integer deleteActionLog(@Param("logId") Integer logId);

    // Other methods can be added as needed...
}