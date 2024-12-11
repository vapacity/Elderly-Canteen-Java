package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BatchResponseDto {

    @JsonProperty("discountDishes")
    private List<DiscountDish> discountDishes;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;
}


