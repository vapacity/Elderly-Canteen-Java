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
public class IngredientLog {
    private Integer logId;
    private String actionType;
    private String ingredientId;
    private Integer remainAmountOld;
    private Integer remainAmountNew;
    private Date expirationTimeOld;
    private Date expirationTimeNew;
    private String changeReason;
    private String changedBy;
    private Date changedAt;
}