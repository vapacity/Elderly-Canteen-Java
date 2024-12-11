package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

}
