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
public class Cart {

    private Integer cartId;           // 购物车ID
    private Integer accountId;        // 账户ID
    private Date createdTime;        // 创建时间
    private Date updatedTime;        // 更新时间

    // 如果需要额外的方法，可以自行添加
}