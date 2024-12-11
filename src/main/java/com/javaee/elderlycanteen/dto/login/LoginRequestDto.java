package com.javaee.elderlycanteen.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("password")
    private String password;
}
