package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface AccountDao {
    @Select("SELECT * FROM Account WHERE accountId = #{accountId} AND password = #{password}")
    Account login(@Param("accountId") String accountId, @Param("password") String password);

    @Select("SELECT * FROM Account WHERE accountId = #{accountId}")
    Account getAccountById(@Param("accountId") String accountId);

    @Select("SELECT * FROM Account")
    List<Account> findAllUsers();

    @Select("SELECT * FROM Account WHERE username = #{username}")
    Account findUserByUsername(String username);

    @Insert("INSERT INTO Account (password, accountName, phoneNum,identity,portrait,gender,money,verifyCode,name,idCard,birthDate,address) VALUES (#{password}, #{accountName}, #{phoneNum},#{identity},#{portrait},#{gender},#{money},#{verifyCode},#{name},#{idCard},#{birthDate},#{address})")
    @Options(useGeneratedKeys=true, keyProperty="accountId")
    Integer insertAccount(Account account);

    @Delete("DELETE FROM Account WHERE id = #{id}")
    int deleteUserById(int id);

    @Update("UPDATE Account SET username = #{username}, password = #{password}, role = #{role} WHERE id = #{id}")
    int updateUser(Account account);

    @Select("SELECT * FROM Account WHERE role = #{role}")
    @Options()
    List<Account> findUserByRole(String role);
}