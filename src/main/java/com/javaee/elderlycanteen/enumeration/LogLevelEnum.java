package com.javaee.elderlycanteen.enumeration;

public enum LogLevelEnum {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private String level;

    LogLevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public static LogLevelEnum getEnum(String level) {
        for (LogLevelEnum logLevel : LogLevelEnum.values()) {
            if (logLevel.getLevel().equals(level)) {
                return logLevel;
            }
        return null;
        }
        return null;
    }
}
