package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AllDiscountResponseDto {

    @JsonProperty("dishes")
    private List<DishDto> dishes;

    // DishDto class
    @Data
    public static class DishDto {

        @JsonProperty("category")
        private String category;

        @JsonProperty("currentPrice")
        private Double currentPrice;

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("originalPrice")
        private Double originalPrice;
    }
}