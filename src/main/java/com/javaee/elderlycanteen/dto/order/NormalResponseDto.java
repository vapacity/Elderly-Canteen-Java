package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NormalResponseDto {
    @JsonProperty("success")
    private String success;

    @JsonProperty("msg")
    private String msg;
}
