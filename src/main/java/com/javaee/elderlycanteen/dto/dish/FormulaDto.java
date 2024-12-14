package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FormulaDto {
    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("ingredientId")
    private Integer ingredientId;

    @JsonProperty("ingredientName")
    private String ingredientName;
}
