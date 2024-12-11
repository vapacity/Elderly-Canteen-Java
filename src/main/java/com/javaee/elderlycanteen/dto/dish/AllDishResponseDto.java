package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaee.elderlycanteen.entity.Formula;
import lombok.Data;

import java.util.List;

@Data
public class AllDishResponseDto {
    @JsonProperty("response")
    public List<DishDto> response;

    @JsonProperty("success")
    public Boolean success;

    @JsonProperty("msg")
    public String msg;
}
