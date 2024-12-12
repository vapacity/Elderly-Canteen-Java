package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Volunteer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VolunteerDao {

    // 根据 accountId 查询单个志愿者对象
    @Select("SELECT * FROM Volunteer WHERE accountId = #{accountId}")
    Volunteer selectByAccountId(Integer accountId);

    // 查询所有志愿者对象
    @Select("SELECT * FROM Volunteer")
    List<Volunteer> selectAll();

    // 插入志愿者对象
    @Insert("INSERT INTO Volunteer(available, score) " +
            "VALUES(#{volunteer.getAvailable()}, #{volunteer.getScore()})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    Integer insert(Volunteer volunteer);

    // 更新志愿者对象
    @Update("UPDATE Volunteer SET " +
            "available = #{volunteer.getAvailable()}, " +
            "score = #{volunteer.getScore()} " +
            "WHERE accountId = #{volunteer.getAccountId()}")
    Integer update(Volunteer volunteer);

    // 根据 accountId 删除志愿者对象
    @Delete("DELETE FROM Volunteer WHERE accountId = #{accountId}")
    Integer deleteByAccountId(Integer accountId);

}