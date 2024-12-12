package com.javaee.elderlycanteen.dto;
import lombok.Data;

@Data
public class ResponseDto<T> {
    private boolean success;  // 请求是否成功
    private String message;   // 请求的返回消息
    private T response;
}
