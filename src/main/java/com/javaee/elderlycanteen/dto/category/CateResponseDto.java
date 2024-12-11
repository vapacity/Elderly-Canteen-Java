package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CateResponseDto {
    @JsonProperty("cates")
    private List<CateRequestDto> cates;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;
}

