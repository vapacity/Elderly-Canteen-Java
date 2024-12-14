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
public class OrderInfo {
    private Integer orderId;
    private String deliverOrDining;
    private Integer financeId;
    private Integer cartId;
    private String status;
    private Double bonus;
    private String remark;
}