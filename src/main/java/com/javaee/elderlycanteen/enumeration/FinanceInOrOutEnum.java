package com.javaee.elderlycanteen.enumeration;

public enum FinanceInOrOutEnum {
    IN("1"),
    OUT("0");
    private String value;

    FinanceInOrOutEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FinanceInOrOutEnum fromValue(String value) {
        for (FinanceInOrOutEnum type : FinanceInOrOutEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
            return null;
        }
        return null;
    }
}
