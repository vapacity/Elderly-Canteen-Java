package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishDto {
    @JsonProperty("dishId")
    private String dishId;

    @JsonProperty("dishName")
    private String dishName;

    @JsonProperty("formula")
    public List<FormulaDto> formula;
}
