package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllCateResponseDto {
    @JsonProperty("cates")
    private List<CateDto> cates;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;
}
