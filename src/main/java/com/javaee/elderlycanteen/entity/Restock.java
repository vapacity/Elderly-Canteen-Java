package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restock {
    private Integer financeId;
    private Integer ingredientId;
    private Integer administratorId;
    private Integer quantity;
    private BigDecimal price;
}