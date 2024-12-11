package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountResponseDto {

    @JsonProperty("dishId")
    private String dishId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("updatedPrice")
    private Double updatedPrice;
}
