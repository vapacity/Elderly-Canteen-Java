package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data            // 自动生成 getter 和 setter 方法，并且生成 toString、equals 和 hashCode 方法
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor // 生成全参构造器
@Builder         // 生成 builder 模式的构造器
public class Account {

    private Long accountId;       // 账户 ID
    private String accountName;   // 账户名
    private String password;      // 密码
    private String phoneNum;      // 手机号
    private String verifyCode;    // 验证码
    private String identity;      // 身份证号
    private String gender;        // 性别
    private Date birthDate;       // 出生日期
    private String address;       // 地址
    private String name;          // 姓名
    private String idCard;        // 身份证号码
    private String portrait;      // 头像
    private Double money;         // 账户余额

    // 如果需要额外的方法，可以自行添加
}

