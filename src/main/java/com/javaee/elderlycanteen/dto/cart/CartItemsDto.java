package com.javaee.elderlycanteen.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CartItemsDto {

    @JsonProperty("menu")
    private List<Menu> menu;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class Menu{
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

        @JsonProperty("quantity")
        private Integer quantity;
    }
}
