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
public class WeekMenu {
    private String dishId;
    private java.util.Date week;
    private Integer stock;
    private Integer sales;
    private BigDecimal disPrice;
    private String day;
}