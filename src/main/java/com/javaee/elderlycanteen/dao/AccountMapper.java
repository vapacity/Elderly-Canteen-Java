package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Account;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM Account WHERE accountId = #{id}")
    Account findUserById(int id);

    @Select("SELECT * FROM Account")
    List<Account> findAllUsers();

    @Select("SELECT COUNT(*) FROM Account WHERE accountId = '123'")
    Integer findUserByAccountId(Long accountId);
}
