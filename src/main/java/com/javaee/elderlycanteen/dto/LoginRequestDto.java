package com.javaee.elderlycanteen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("password")
    private String password;
}
