package com.javaee.elderlycanteen.enumeration;

public enum FinanceTypeEnum {
    ORDER("Order");

    private String description;  // 英文描述

    // 构造方法
    FinanceTypeEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static FinanceTypeEnum fromDescriptionEn(String description) {
        for (FinanceTypeEnum status : FinanceTypeEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
