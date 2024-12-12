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
public class RepositoryLog {
    private Integer logId;
    private String actionType;
    private String entityType;
    private Integer entityId;
    private String oldValue;
    private String newValue;
    private Date changedAt;
    private String changedBy;
}