package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AdminInfoChangeDto {
    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthDate")
    private Date birthDate;

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;
}
