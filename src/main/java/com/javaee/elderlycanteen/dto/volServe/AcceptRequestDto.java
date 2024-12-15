package com.javaee.elderlycanteen.dto.volServe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcceptRequestDto {

    @JsonProperty("orderId")
    private Integer orderId;

}
