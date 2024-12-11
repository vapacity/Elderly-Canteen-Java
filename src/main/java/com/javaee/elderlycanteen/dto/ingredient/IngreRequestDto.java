package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IngreRequestDto {
    @JsonProperty("ingredientId")
    private String ingredientId;

    @JsonProperty("ingredientName")
    private String ingredientName;
}
