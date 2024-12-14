package com.javaee.elderlycanteen.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CateDto {
    @JsonProperty("cateId")
    private Integer cateId;
    @JsonProperty("cateName")
    private String cateName;
}
