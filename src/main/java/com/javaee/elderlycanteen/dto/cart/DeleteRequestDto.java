package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteRequestDto {
    @JsonProperty("cartId")
    private String cartId; // 购物车ID

    @JsonProperty("dishId")
    private String dishId; // 菜品ID
}
