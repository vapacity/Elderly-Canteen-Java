package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDish {

    @JsonProperty("dishName")
    private String dishName;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private Integer quantity;
}
