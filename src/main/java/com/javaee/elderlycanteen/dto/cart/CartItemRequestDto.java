package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @JsonProperty("CART_ID")
    private String cartId;

    @JsonProperty("DISH_ID")
    private String dishId;

    @JsonProperty("QUANTITY")
    private Integer quantity;
}
