package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Menu {

    @JsonProperty("discountPrice")
    private Double discountPrice;

    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("dishName")
    private String dishName;

    @JsonProperty("dishPrice")
    private Double dishPrice;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("sales")
    private Integer sales;

    @JsonProperty("stock")
    private Integer stock;

    @JsonProperty("category")
    private Integer category;
}
