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
public class Dish {

    private String dishId;           // 菜品ID
    private String dishName;         // 菜品名称
    private BigDecimal price;        // 价格
    private String cateId;           // 类别ID
    private String imageUrl;         // 图片URL

    // 如果需要额外的方法，可以自行添加
}