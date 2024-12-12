package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekMenu {
    private Integer dishId;
    private Date week;
    private Integer stock;
    private Integer sales;
    private BigDecimal disPrice;
    private String day;
}