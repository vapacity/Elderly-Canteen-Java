package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private OrderItem[] response;
}
