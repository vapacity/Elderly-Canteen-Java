package com.javaee.elderlycanteen.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponseDto {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private ResponseData response;

    @Data
    public static class ResponseData {
        @JsonProperty("token")
        private String token;

        @JsonProperty("identity")
        private String identity;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("accountId")
        private String accountId;
    }
}

