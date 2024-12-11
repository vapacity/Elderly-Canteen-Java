package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @JsonProperty("cartId")
    private String cartId;

    @JsonProperty("dishId")
    private String dishId;

    @JsonProperty("quantity")
    private Integer quantity;
}
