package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IngreResponseDto {
    @JsonProperty("IngredientId")
    private String ingredientId;

    @JsonProperty("IngredientName")
    private String ingredientName;

}
