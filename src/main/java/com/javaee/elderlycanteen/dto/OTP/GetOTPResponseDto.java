package com.javaee.elderlycanteen.dto.OTP;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetOTPResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;
}
