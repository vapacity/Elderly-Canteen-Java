package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliverOrder {

    private Integer orderId;
    private String deliverPhone;
    private String customerPhone;
    private String cusAddress;
    private String deliverStatus;
    private Integer cartId;
}