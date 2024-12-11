package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllIngreResponseDto {
    @JsonProperty("ingredients")
    public List<IngredientDto> ingredients;
}
