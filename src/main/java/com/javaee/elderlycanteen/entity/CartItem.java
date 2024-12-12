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
public class CartItem {

    private Integer cartId;           // 购物车ID
    private Integer dishId;           // 菜品ID
    private Date week;               // 周
    private Integer quantity;        // 数量

    // 如果需要额外的方法，可以自行添加
}