package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM Account WHERE id = #{id}")
    Account findUserById(int id);

    @Select("SELECT * FROM Account")
    List<Account> findAllUsers();

    @Select("SELECT * FROM Account WHERE username = #{username}")
    Account findUserByUsername(String username);

    @Insert("INSERT INTO Account (username, password, role) VALUES (#{username}, #{password}, #{role})")
    int insertUser(Account account);

    @Delete("DELETE FROM Account WHERE id = #{id}")
    int deleteUserById(int id);

    @Update("UPDATE Account SET username = #{username}, password = #{password}, role = #{role} WHERE id = #{id}")
    int updateUser(Account account);

    @Select("SELECT * FROM Account WHERE role = #{role}")
    List<Account> findUserByRole(String role);
}
