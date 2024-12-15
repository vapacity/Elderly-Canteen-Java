package com.javaee.elderlycanteen.enumeration;

public enum DeliverOrDiningEnum {
    DELIVER("D"),   // 待审核
    DINING("I");

    private String description;  // 英文描述

    // 构造方法
    DeliverOrDiningEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static DeliverOrDiningEnum fromDescriptionEn(String description) {
        for (DeliverOrDiningEnum status : DeliverOrDiningEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
