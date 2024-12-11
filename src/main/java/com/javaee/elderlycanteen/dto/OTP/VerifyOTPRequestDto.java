package com.javaee.elderlycanteen.dto.OTP;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyOTPRequestDto {
    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("code")
    private String code;

    @JsonProperty("newPassword")
    private String newPassword;

    @JsonProperty("newPhoneNum")
    private String newPhoneNum;
}
