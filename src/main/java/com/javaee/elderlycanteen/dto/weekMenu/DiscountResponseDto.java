package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponseDto {

    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("updatedPrice")
    private Double updatedPrice;
}
