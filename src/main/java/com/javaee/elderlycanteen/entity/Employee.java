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
public class Employee {
    private Integer employeeId;
    private String employeeName;
    private String phoneNum;
    private String address;
    private String employeePosition;
    private Double salary;
    private String idCard;
    private Boolean isPaidThisMonth;
}