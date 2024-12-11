package com.javaee.elderlycanteen.dto.OTP;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyOTPResponseDto<T> {

    @JsonProperty("success")
    private Boolean success;  // Boolean to handle nullability

    @JsonProperty("msg")
    private String msg;       // Required field (non-nullable)

    @JsonProperty("response")
    private T response;       // Generic response (nullable)

    @Data
    public static class OTPLoginResponseDto {

        @JsonProperty("token")
        private String token;     // Optional field (nullable)

        @JsonProperty("identity")
        private String identity;  // Optional field (nullable)

        @JsonProperty("accountName")
        private String accountName; // Optional field (nullable)

        @JsonProperty("accountId")
        private String accountId;   // Optional field (nullable)
    }
}

