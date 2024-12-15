package com.javaee.elderlycanteen.enumeration;

public enum OrderStatusEnum {
    TOBECONFIRMED("To be confirmed"),   // 待审核
    PENDING("Pending"),  // 已通过
    DINING("Dining"),
    CONFIRMED("Confirmed"),
    REVIEWED("Reviewed"); // 已拒绝

    private String description;  // 英文描述

    // 构造方法
    OrderStatusEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static OrderStatusEnum fromDescriptionEn(String description) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
