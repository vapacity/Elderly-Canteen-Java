package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @JsonProperty("cartId")
    private Integer cartId;

    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("quantity")
    private Integer quantity;
}
