package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterResponseDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("timestamp")
    private String timestamp; // This will handle null as String is nullable by default in Java

    @JsonProperty("response")
    private RegisterResponse response;

    @Data
    public static class RegisterResponse {

        @JsonProperty("token")
        private String token;

        @JsonProperty("identity")
        private String identity;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("accountId")
        private Integer accountId;
    }
}


