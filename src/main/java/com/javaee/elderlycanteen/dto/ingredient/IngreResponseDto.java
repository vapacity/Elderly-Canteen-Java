package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IngreResponseDto {

    @JsonProperty("success")
    public Boolean success;

    @JsonProperty("msg")
    public String msg;

    @JsonProperty("ingredientId")
    private Integer ingredientId;

    @JsonProperty("ingredientName")
    private String ingredientName;

}
