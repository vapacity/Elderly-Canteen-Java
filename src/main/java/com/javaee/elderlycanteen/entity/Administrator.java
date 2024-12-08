package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrator {

    private String accountId;        // 账户ID
    private String email;            // 邮箱
    private String position;         // 职位

    // 如果需要额外的方法，可以自行添加
}