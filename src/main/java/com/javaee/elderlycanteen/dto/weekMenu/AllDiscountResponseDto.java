package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDiscountResponseDto {

    @JsonProperty("dishes")
    private List<DishDto> dishes;

    // DishDto class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DishDto {

        @JsonProperty("category")
        private String category;

        @JsonProperty("currentPrice")
        private Double currentPrice;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("originalPrice")
        private Double originalPrice;
    }
}