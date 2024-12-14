package com.javaee.elderlycanteen.dao;


import com.javaee.elderlycanteen.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.Date;
import java.util.List;

@Mapper
public interface AccountDao {
    @Select("SELECT * FROM Account WHERE phoneNum = #{phoneNum} AND password = #{password}")
    Account login(@Param("phoneNum") String phoneNum, @Param("password") String password);

    @Select("SELECT * FROM Account WHERE accountId = #{accountId}")
    Account getAccountById(@Param("accountId") Integer accountId);

    @Select("SELECT * FROM Account")
    List<Account> findAllUsers();

    @Insert("INSERT INTO Account (password, accountName, phoneNum,identity,portrait,gender,money,verifyCode,name,idCard,birthDate,address) VALUES (#{password}, #{accountName}, #{phoneNum},#{identity},#{portrait},#{gender},#{money},#{verifyCode},#{name},#{idCard},#{birthDate},#{address})")
    @Options(useGeneratedKeys=true, keyProperty="accountId")
    Integer insertAccount(Account account);

    @Delete("DELETE FROM Account WHERE id = #{id}")
    int deleteUserById(int id);

    // 修改认证信息
   @Update("UPDATE Account SET identity = #{ indentity } , name = #{name}, birthDate = #{birthDate} ,idCard = #{idCard} WHERE accountId = #{accountId}")
    Integer updatePersonIdentity(@Param("indentity") String identity , @Param("name") String name, @Param("birthDate") Date birthDate , @Param("idCard") String idCard, @Param("accountId") Integer accountId);

    //修改密码
    @Update("UPDATE Account SET password = #{ password } WHERE accountId = #{accountId}")
    Integer changePassword(@Param("password") String password,@Param("accountId") Integer accountId);

    //修改手机号
    @Update("UPDATE Account SET phoneNum = #{phoneNum} WHERE accountId = #{accountId}")
    Integer changePhoneNum(@Param("phoneNum") String phoneNum,@Param("accountId") Integer accountId);

    //充值
    @Update("UPDATE Account SET money = #{money} WHERE accountId = #{accountId}")
    Integer creditAccount(@Param("money") Double money,@Param("accountId") Integer accountId);

    //修改个人信息
    @Update("UPDATE Account SET portrait = #{portrait},gender = #{gender} , accountName = #{accountName},phoneNum = #{phoneNum},address = #{address},birthDate = #{birthDate}, name = #{name} WHERE accountId = #{accountId} ")
    Integer updatePersonInfo(@Param("portrait") String portrait,@Param("gender") String gender,@Param("accountName") String accountName , @Param("phoneNum") String phoneNum , @Param("address") String address ,@Param("birthDate") Date birthDate ,@Param("name") String name);

    //查询是否存在这个身份证号
    @Select("SELECT COUNT(*) > 0 FROM Account WHERE idCard = #{idCard}")
    boolean existsByIdCard(@Param("idCard") String idCard);

    // 查询是否有相同的手机号，但排除当前账户
    @Select("SELECT * FROM Account WHERE phoneNum = #{phoneNum} AND accountId != #{accountId}")
    Account findByPhoneNum(@Param("phoneNum") String phoneNum, @Param("accountId") Integer accountId);

    @Delete("DELETE  FROM Account WHERE accountId = #{accountId} ")
    Integer deleteUserFromAccount(@Param("accountId") Integer accountId);

    //更改头像
    @Update("UPDATE Account SET portrait = #{portrait} WHERE accountId = #{accountId}")
    Integer updatePortrait(@Param("portrait") String portrait,@Param("accountId") Integer accountId);

    @Select("SELECT * FROM Account WHERE role = #{role}")
    @Options()
    List<Account> findUserByRole(String role);

    @Update("UPDATE Account SET money=#{money} WHERE accountId=#{accountId}")
    Integer updateAccountMoney(Integer accountId, Double money);

    @Update("UPDATE Account SET address=#{address} WHERE accountId=#{accountId}")
    Integer updateAccountAddress(Integer accountId, String address);

}