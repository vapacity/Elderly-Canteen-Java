package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.config.DateDeserializer;
import com.javaee.elderlycanteen.config.DateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequestDto {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("verificationCode")
    private Integer verificationCode;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthDate")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date birthDate;
}
