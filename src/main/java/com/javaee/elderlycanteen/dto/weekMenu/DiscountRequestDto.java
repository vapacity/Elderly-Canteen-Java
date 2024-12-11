package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiscountRequestDto {

    @JsonProperty("date")
    private Date date;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("dishId")
    private String dishId;
}
