package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IngreRequestDto {
    @JsonProperty("ingredientId")
    @Schema(description = "食材ID", example = "1")
    private Integer ingredientId;

    @JsonProperty("ingredientName")
    @Schema(description = "食材名稱", example = "·1")
    private String ingredientName;
}
