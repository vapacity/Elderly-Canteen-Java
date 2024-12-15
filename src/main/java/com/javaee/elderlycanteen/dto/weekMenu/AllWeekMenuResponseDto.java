package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaee.elderlycanteen.entity.WeekMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllWeekMenuResponseDto {
    @JsonProperty("weekMenuList")
    private List<List<WeekDish>> weekMenuList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeekDish {
        @JsonProperty("category")
        private String category;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("originalPrice")
        private Double originalPrice;

        @JsonProperty("discountPrice")
        private Double discountPrice;
    }
}


