package com.javaee.elderlycanteen.dto.account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AccountDto {
    // 必须字段
    @JsonProperty("accountId")
    private String accountId; // 账号ID，必须

    @JsonProperty("accountName")
    private String accountName; // 账号名称，必须

    @JsonProperty("phoneNum")
    private String phoneNum; // 电话号码，必须

    @JsonProperty("identity")
    private String identity; // 身份类型，必须

    @JsonProperty("portrait")
    private String portrait; // 头像URL，必须

    @JsonProperty("gender")
    private String gender; // 性别，必须

    @JsonProperty("money")
    private BigDecimal money; // 金额

    // 非必须字段
    @JsonProperty("birthDate")
    private Date birthDate; // 出生日期，可选

    @JsonProperty("address")
    private String address; // 地址，可选

    @JsonProperty("name")
    private String name; // 姓名，可选

    @JsonProperty("password")
    private String password; // 密码，可选

    @JsonProperty("Idcard")
    private String idCard; // 身份证号，可选
}
