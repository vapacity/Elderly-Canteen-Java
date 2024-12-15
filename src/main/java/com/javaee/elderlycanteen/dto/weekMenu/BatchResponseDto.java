package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponseDto {

    @JsonProperty("discountDishes")
    private List<DiscountDish> discountDishes;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;
}


