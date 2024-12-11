package com.javaee.elderlycanteen.dto.OTP;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetOTPRequestDto {
    @JsonProperty("phoneNum")
    private String phoneNum;
}
