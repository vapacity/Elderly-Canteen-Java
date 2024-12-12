package com.javaee.elderlycanteen.exception;

public class ServiceException extends RuntimeException {
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
