package com.javaee.elderlycanteen.dto.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AudioResponseDto {
    private int code;
    private String message;
    private String sid;
    @JsonProperty("data")
    private Reply data;
}

