package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountDish {

    @JsonProperty("discountPrice")
    private Double discountPrice;

    @JsonProperty("dishId")
    private String dishId;

    @JsonProperty("dishName")
    private String dishName;
}
