package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.config.DateDeserializer;
import com.javaee.elderlycanteen.config.DateSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiscountRequestDto {

    @JsonProperty("date")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("dishId")
    private Integer dishId;
}
