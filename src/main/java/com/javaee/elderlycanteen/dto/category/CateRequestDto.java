package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CateRequestDto {
    @JsonProperty("cateId")
    private Integer cateId;

    @JsonProperty("cateName")
    private String cateName;

}
