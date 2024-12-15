package com.javaee.elderlycanteen.enumeration;

public enum FinanceStatusEnum {

    PENDING("Pending"),   // 待审核
    APPROVED("Approved"),  // 已通过
    REJECTED("Rejected"); // 已拒绝

    private String description;  // 英文描述

    // 构造方法
    FinanceStatusEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static FinanceStatusEnum fromDescriptionEn(String description) {
        for (FinanceStatusEnum status : FinanceStatusEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
