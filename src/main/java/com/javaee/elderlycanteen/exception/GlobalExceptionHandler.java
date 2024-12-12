package com.javaee.elderlycanteen.exception;

import com.javaee.elderlycanteen.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> handleIllegalAccessException(IllegalAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseDto<?>> handleServiceException(ServiceException ex) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST); // 400 错误，表示请求的参数有问题
    }

    // 处理 NotFoundException 异常
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<?>> handleNotFoundException(NotFoundException ex) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND); // 404 错误，表示资源未找到
    }

    // 处理 InvalidInputException 异常
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDto<?>> handleInvalidInputException(InvalidInputException ex) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST); // 400 错误，表示输入不合法
    }

    // 处理其他未知异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleException(Exception ex) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setMessage("Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR); // 返回 500 错误
    }
}
