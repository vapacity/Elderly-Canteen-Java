package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDish {

    @JsonProperty("discountPrice")
    private Double discountPrice;

    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("dishName")
    private String dishName;
}
