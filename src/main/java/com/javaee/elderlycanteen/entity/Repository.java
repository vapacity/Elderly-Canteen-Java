package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {
    private String ingredientId;
    private Integer remainAmount;
    private Integer highConsumption;
    private Date expirationTime;
}