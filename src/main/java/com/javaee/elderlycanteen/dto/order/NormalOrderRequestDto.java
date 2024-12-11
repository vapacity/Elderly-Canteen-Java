package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NormalOrderRequestDto {
    @JsonProperty("orderId")
    private String orderId;
}
