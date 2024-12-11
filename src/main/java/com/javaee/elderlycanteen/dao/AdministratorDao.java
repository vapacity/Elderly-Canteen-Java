package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AdministratorDao {
    //GET
    //根据accountId查询管理员信息
    @Select("SELECT * FROM Administrator WHERE accountId = #{accountId}")
    Administrator GetAdminById(@Param("accountId") String accountId);

    //根据email和position搜索管理员
    @Select("SELECT * FROM Administrator WHERE email = #{email} OR position = #{position}")
    List<Administrator> SearchAdmin(@Param("email") String email, @Param("position") String position);

    //PUT
    //更新管理员的email和position
    @Update("UPDATE Administrator SET email = #{email}, position = #{position} WHERE accountId = #{accountId}")
    Integer UpdateAdmin(@Param("accountId") String accountId, @Param("email") String email, @Param("position") String position);

    //POST
    //插入新的管理员记录
    @Insert("INSERT INTO Administrator (accountId, email, position) VALUES (#{accountId}, #{email}, #{position})")
    Integer AddAdmin(@Param("accountId") String accountId, @Param("email") String email, @Param("position") String position);

    //DELETE
    //根据accountId删除管理员记录
    @Delete("DELETE FROM Administrator WHERE accountId = #{accountId}")
    Integer DeleteAdmin(@Param("accountId") String accountId);
}