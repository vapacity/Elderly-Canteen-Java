package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteRequestDto {
    @JsonProperty("CART_ID")
    private String cartId; // 购物车ID

    @JsonProperty("DISH_ID")
    private String dishId; // 菜品ID
}
