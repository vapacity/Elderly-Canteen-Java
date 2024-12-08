package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    private String cateId;           // 类别ID
    private String cateName;         // 类别名称

    // 如果需要额外的方法，可以自行添加
}