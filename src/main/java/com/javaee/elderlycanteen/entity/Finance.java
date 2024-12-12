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
public class Finance {
    private Integer financeId;
    private String financeType;
    private java.util.Date financeDate;
    private BigDecimal price;
    private String inOrOut;
    private Integer accountId;
    private Integer administratorId;
    private byte[] proof;
    private String status;
}