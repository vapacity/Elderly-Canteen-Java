package com.javaee.elderlycanteen.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Account {

    private Integer accountId;
    private String accountName;     // 账户名
    private String password;        // 密码
    private String phoneNum;        // 手机号
    private Integer verifyCode;     // 验证码
    private String identity;        // 身份证号
    private Date birthDate;         // 出生日期
    private String address;         // 地址
    private String name;            // 姓名
    private String idCard;          // 身份证号码
    private String portrait;        // 头像
    private Double money;       // 账户余额
    private String gender;

}

