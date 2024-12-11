package com.javaee.elderlycanteen.dto.personInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PhoneResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;
}
