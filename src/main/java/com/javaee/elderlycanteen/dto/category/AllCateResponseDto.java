package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllCateResponseDto {
    @JsonProperty("cates")
    private List<Cate> cates;

    @Data
    public static class Cate {
        @JsonProperty("cateId")
        private String cateId;

        @JsonProperty("cateName")
        private String cateName;
    }
}
