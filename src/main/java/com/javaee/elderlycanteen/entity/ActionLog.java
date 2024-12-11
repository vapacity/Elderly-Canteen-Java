package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLog {

    private Integer logId;           // 日志ID
    private String actionType;       // 操作类型
    private String entityType;       // 实体类型
    private Integer entityId;        // 实体ID
    private String oldValue;         // 旧值
    private String newValue;         // 新值
    private Timestamp changedAt;     // 变更时间
    private String changedBy;        // 变更人

    // 如果需要额外的方法，可以自行添加
}
