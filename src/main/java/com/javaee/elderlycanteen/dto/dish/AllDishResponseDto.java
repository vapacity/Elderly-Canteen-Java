package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaee.elderlycanteen.entity.Formula;
import lombok.Data;

import java.util.List;

@Data
public class AllDishResponseDto {
    @JsonProperty("response")
    public List<DishDto> dish;
}
