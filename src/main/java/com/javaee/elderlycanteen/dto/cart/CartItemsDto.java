package com.javaee.elderlycanteen.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        private Integer dishId;

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
