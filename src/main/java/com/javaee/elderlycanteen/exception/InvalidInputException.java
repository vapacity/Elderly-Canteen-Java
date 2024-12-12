package com.javaee.elderlycanteen.exception;

public class InvalidInputException extends RuntimeException {
    private String message;

    public InvalidInputException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

