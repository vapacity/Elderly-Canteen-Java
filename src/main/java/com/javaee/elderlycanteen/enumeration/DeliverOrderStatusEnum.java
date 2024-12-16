package com.javaee.elderlycanteen.enumeration;

public enum DeliverOrderStatusEnum {
    DELIVER_PENDING("Pending"),   // 待审核
    DELIVER_DELIVERED("Delivered"),  // 已通过
    DELIVER_RECEIVED("Received"); // 已拒绝

    private String description;  // 英文描述

    // 构造方法
    DeliverOrderStatusEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static DeliverOrderStatusEnum fromDescriptionEn(String description) {
        for (DeliverOrderStatusEnum status : DeliverOrderStatusEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
