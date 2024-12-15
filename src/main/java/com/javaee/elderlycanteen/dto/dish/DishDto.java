package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("dishName")
    private String dishName;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("cateId")
    private Integer cateId;

    @JsonProperty("cateName")
    private String cateName;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("formula")
    public List<FormulaDto> formula;
}
