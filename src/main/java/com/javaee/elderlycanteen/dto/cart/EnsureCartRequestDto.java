package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class EnsureCartRequestDto {
    @JsonProperty("cartId")
    private Integer cartId;

    @JsonProperty("newAddress")
    private String newAddress;

    @JsonProperty("deliverOrDining")
    private Boolean deliverOrDining;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("setDefaultAdd")
    private Boolean setDefaultAdd;
}
