package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("verificationCode")
    private String verificationCode;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthDate")
    private String birthDate;
}
