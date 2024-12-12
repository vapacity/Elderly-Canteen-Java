package com.javaee.elderlycanteen.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String message;
    private Boolean success;
    private T data;
}
