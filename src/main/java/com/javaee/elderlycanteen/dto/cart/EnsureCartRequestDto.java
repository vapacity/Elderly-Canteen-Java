package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnsureCartRequestDto {
    @JsonProperty("CART_ID")
    private String cartId;

    @JsonProperty("newAddress")
    private String newAddress;

    @JsonProperty("deliver_or_dining")
    private Boolean deliverOrDining;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("set_default_add")
    private Boolean setDefaultAdd;
}
