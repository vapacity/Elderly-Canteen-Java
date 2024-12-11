package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishRequestDto {
    @JsonProperty("cateId")
    private String cateId;

    @JsonProperty("formula")
    private List<FormulaDto> formula;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("dishId")
    private String dishId;


}
