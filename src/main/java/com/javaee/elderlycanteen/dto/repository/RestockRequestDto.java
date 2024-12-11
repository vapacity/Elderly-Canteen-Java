package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RestockRequestDto {

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("expiry")
    private Date expiry;

    @JsonProperty("ingredientId")
    private String ingredientId;

    @JsonProperty("price")
    private Double price;
}