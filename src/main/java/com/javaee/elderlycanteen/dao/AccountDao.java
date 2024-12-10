package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Account;
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

    // 登录 - 根据账号和密码查询用户
    @Select("SELECT * FROM Account WHERE accountName = #{accountName} AND password = #{password}")
    Account login(@Param("accountName") String accountName, @Param("password") String password);

    // 注册 - 插入新用户
    @Insert("INSERT INTO Account (accountName, password, phoneNum, identity, birthDate, address, name, idCard, portrait, money) " +
            "VALUES (#{accountName}, #{password}, #{phoneNum}, #{identity}, #{birthDate}, #{address}, #{name}, #{idCard}, #{portrait}, #{money})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    int register(Account account);

    // 获取个人信息
    @Select("SELECT * FROM Account WHERE accountId = #{accountId}")
    Account getPersonInfo(@Param("accountId") String accountId);

    // 修改个人信息
    @Update("UPDATE Account SET accountName = #{accountName}, phoneNum = #{phoneNum}, identity = #{identity}, " +
            "birthDate = #{birthDate}, address = #{address}, name = #{name}, idCard = #{idCard}, portrait = #{portrait} WHERE accountId = #{accountId}")
    int alterPersonInfo(@Param("accountId") String accountId, @Param("accountName") String accountName,
                        @Param("phoneNum") String phoneNum, @Param("identity") String identity, @Param("birthDate") Date birthDate,
                        @Param("address") String address, @Param("name") String name, @Param("idCard") String idCard,
                        @Param("portrait") String portrait);

    // 获取所有账户
    @Select("SELECT * FROM Account")
    List<Account> getAllAccounts();

    // 名称认证 - 根据名称查询用户
    @Select("SELECT * FROM Account WHERE accountName = #{accountName}")
    List<Account> nameAutenticate(@Param("accountName") String accountName);

    // 修改密码
    @Update("UPDATE Account SET password = #{newPassword} WHERE accountId = #{accountId}")
    int changePassword(@Param("accountId") String accountId, @Param("newPassword") String newPassword);

    // 请求验证码 - 通常发送验证码到用户手机或邮箱
    // 注意：此方法可能需要实现业务逻辑，而不是简单的SQL操作
    int requestVerificationCode(@Param("request") GetOTPRequestDto request);

    // 验证登录验证码
    @Update("UPDATE Account SET verifyCode = #{verifyCode} WHERE phoneNum = #{phoneNum} AND verifyCode = #{inputCode}")
    int verifyLoginOTP(@Param("phoneNum") String phoneNum, @Param("verifyCode") Integer verifyCode, @Param("inputCode") Integer inputCode);

    // 验证验证码（无需用户检查）
    @Update("UPDATE Account SET verifyCode = #{verifyCode} WHERE phoneNum = #{phoneNum} AND verifyCode = #{inputCode}")
    int verifyOTPWithoutUserCheck(@Param("phoneNum") String phoneNum, @Param("verifyCode") Integer verifyCode, @Param("inputCode") Integer inputCode);

    // 更改手机号
    @Update("UPDATE Account SET phoneNum = #{newPhoneNum} WHERE accountId = #{accountId}")
    int changePhone(@Param("accountId") String accountId, @Param("newPhoneNum") String newPhoneNum);

    // 删除用户
    @Delete("DELETE FROM Account WHERE accountId = #{accountId}")
    int deleteUser(@Param("accountId") String accountId);

    // 预付费充值
    @Update("UPDATE Account SET money = money + #{money} WHERE accountId = #{accountId}")
    int creditAccount(@Param("accountId") String accountId, @Param("money") BigDecimal money);
}