package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CateResponseDto {
    @JsonProperty("cates")
    private List<CateDto> cates;

    @Data
    public static class CateDto {
        @JsonProperty("cateId")
        private String cateId;

        @JsonProperty("cateName")
        private String cateName;
    }
}
