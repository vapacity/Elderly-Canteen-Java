package com.javaee.elderlycanteen.enumeration;

public enum AccountIdentityEnum {
    SENIOR("Senior");

    private String description;  // 英文描述

    // 构造方法
    AccountIdentityEnum(String description) {
        this.description = description;
    }

    // 获取英文描述
    public String getDescription() {
        return description;
    }

    // 通过英文描述获取对应的 enum 值
    public static AccountIdentityEnum fromDescriptionEn(String description) {
        for (AccountIdentityEnum status : AccountIdentityEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + description);
    }
}
