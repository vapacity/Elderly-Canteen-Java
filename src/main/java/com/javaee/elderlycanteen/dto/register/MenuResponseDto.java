package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuResponseDto {

    @JsonProperty("menu")
    private List<Menu> menu;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class Menu {

        @JsonProperty("discountPrice")
        private Double discountPrice;

        @JsonProperty("dishId")
        private String dishId;

        @JsonProperty("dishName")
        private String dishName;

        @JsonProperty("dishPrice")
        private Double dishPrice;

        @JsonProperty("imageUrl")
        private String imageUrl;

        @JsonProperty("sales")
        private Integer sales;

        @JsonProperty("stock")
        private Integer stock;

        @JsonProperty("category")
        private String category;
    }
}
