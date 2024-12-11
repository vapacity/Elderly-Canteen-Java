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

}