package com.javaee.elderlycanteen.dto.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReduceResponseDto {
    String dishName;
    int quantity;
    List<ReduceIngreDto> ingredientCost;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReduceIngreDto{
        String ingredientName;
        int ingredientQuantity;
        int remainingQuantity;
    }
}
