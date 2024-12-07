package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM Account WHERE id = #{id}")
    Account findUserById(int id);

    @Select("SELECT * FROM Account")
    List<Account> findAllUsers();
}
