package com.javaee.elderlycanteen.dto.order;

import java.io.Serializable;

/**
 * SEC-LAB-A3：离线订单快照，使用 Java 原生序列化传输。
 */
public class OrderSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderId;
    private String remark;
    private Double totalPrice;

    public OrderSnapshot() {
    }

    public OrderSnapshot(Integer orderId, String remark, Double totalPrice) {
        this.orderId = orderId;
        this.remark = remark;
        this.totalPrice = totalPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
