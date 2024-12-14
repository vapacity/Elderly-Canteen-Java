package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private OrderItem response;
}

