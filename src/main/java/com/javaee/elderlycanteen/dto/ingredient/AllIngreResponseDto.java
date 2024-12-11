package com.javaee.elderlycanteen.dto.ingredient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllIngreResponseDto {
    @JsonProperty("ingredients")
    public List<IngreRequestDto> ingredients;

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;
}
