package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.dto.admin.AdminSearchDto;
import com.javaee.elderlycanteen.entity.Administrator;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AdministratorDao {
    //GET
    //根据accountId查询管理员信息
    @Select("SELECT * FROM Administrator WHERE accountId = #{accountId}")
    Administrator getAdminById(@Param("accountId") Integer accountId);

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

    //添加管理员
    @Insert("INSERT INTO Administrator (accountId, email ,position) VALUES (#{accountId}, #{email},#{position})")
    Integer addAdmin(Administrator admin);

    //删除管理员
    @Delete("DELETE FROM Administrator WHERE accountId = #{accountId}")
    Integer deleteAdmin(Integer accountId);



    /**
     * 根据名字和职位查询管理员列表
     * @param name 管理员名字
     * @param position 管理员职位
     * @return 返回管理员及其关联账户的信息列表
     */
    @Select("""
        SELECT 
            a.accountId AS accountId,
            a.position AS position,
            ac.name AS name,
            ac.phoneNum AS phoneNum,
            ac.gender AS gender
        FROM Administrator a
        JOIN Account ac ON a.accountId = ac.accountId
        WHERE 
            (#{name} IS NULL OR ac.name LIKE CONCAT('%', #{name}, '%'))
            AND 
            (#{position} IS NULL OR a.position LIKE CONCAT('%', #{position}, '%'))
    """)
    AdminSearchDto findAdminsByNameAndPosition(
            @Param("name") String name,
            @Param("position") String position
    );
}