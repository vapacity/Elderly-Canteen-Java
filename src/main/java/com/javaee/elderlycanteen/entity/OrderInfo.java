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
    private String orderId;
    private String deliverOrDining;
    private String financeId;
    private String cartId;
    private String status;
    private BigDecimal bonus;
    private String remark;
}