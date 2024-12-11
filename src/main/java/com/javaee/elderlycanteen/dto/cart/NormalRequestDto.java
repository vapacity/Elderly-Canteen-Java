package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NormalRequestDto {
    @JsonProperty("cartId")
    private String cartId;
}
