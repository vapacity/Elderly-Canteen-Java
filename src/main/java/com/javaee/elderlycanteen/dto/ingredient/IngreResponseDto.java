package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IngreResponseDto {

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;

    @JsonProperty("ingredientId")
    private String ingredientId;

    @JsonProperty("ingredientName")
    private String ingredientName;

}
