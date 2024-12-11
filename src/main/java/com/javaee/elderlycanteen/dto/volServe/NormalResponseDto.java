package com.javaee.elderlycanteen.dto.volServe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NormalResponseDto {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("msg")
    private String msg;
}
