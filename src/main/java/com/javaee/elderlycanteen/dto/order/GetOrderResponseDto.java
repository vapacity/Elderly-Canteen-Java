package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

@Data
public class GetOrderResponseDto {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private OrderItem[] response;
}
