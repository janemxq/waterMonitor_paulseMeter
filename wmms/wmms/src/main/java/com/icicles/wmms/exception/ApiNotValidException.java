package com.icicles.wmms.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * api异常模板类
 * @author lisy
 */
@Data
public class ApiNotValidException implements Serializable {
    private String message;
    private String path;
    private int status;
    private Long timestamp;
    private String error;

    public ApiNotValidException(HttpServletRequest request,String message,HttpStatus code){
        this.message = message;
        this.path = request.getServletPath();
        this.status = code.value();
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        this.error = code.toString();
    }

    public ApiNotValidException(HttpServletRequest request, BindingResult bindingResult){
        StringBuilder message = new StringBuilder();
        bindingResult.getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            message.append(errorMessage).append(" ");
        });
        this.message = message.toString();
        this.path = request.getServletPath();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        this.error = HttpStatus.BAD_REQUEST.toString();
    }
}
