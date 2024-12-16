package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.serializer.DateDeserializer;
import com.javaee.elderlycanteen.serializer.DateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class RestockRequestDto {

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("expiry")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date expiry;

    @JsonProperty("ingredientId")
    private Integer ingredientId;

    @JsonProperty("price")
    private Double price;
}