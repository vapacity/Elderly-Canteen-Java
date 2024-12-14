package com.javaee.elderlycanteen.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("cartId")
    private Integer cartId;           // 购物车ID
    @JsonProperty("accountId")
    private Integer accountId;        // 账户ID
    @JsonProperty("createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;        // 创建时间
    @JsonProperty("updatedTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedTime;        // 更新时间
    // 如果需要额外的方法，可以自行添加
}