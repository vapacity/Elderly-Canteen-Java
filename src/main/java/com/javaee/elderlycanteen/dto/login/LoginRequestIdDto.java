package com.javaee.elderlycanteen.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestIdDto {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("password")
    private String password;
}
