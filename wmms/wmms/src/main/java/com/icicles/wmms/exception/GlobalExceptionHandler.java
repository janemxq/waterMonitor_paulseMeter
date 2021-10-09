package com.icicles.wmms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 * @author lisy
 */
@ControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 一般性的异常
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(HttpServletRequest request,Exception e) {
        if(e instanceof ApiException) {
            ApiException ex = (ApiException)e;
            ApiNotValidException apiNotValidException = new ApiNotValidException(request,ex.getMessage(),ex.getStatus());
            return new ResponseEntity<>(apiNotValidException, ex.getStatus());
        }else{
            //一般性错误
            logger.error(e.getMessage());
            ApiNotValidException apiNotValidException = new ApiNotValidException(request,"系统异常，请联系管理员",HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(apiNotValidException,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 一般性的表单验证异常(前端json格式提交)
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleParameterException(HttpServletRequest request, MethodArgumentNotValidException e) {
        ApiNotValidException apiNotValidException = new ApiNotValidException(request,e.getBindingResult());
        return new ResponseEntity<>(apiNotValidException, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数验证异常(前端form表单格式提交)
     * @param exception 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handlerBindException(HttpServletRequest request,BindException exception) {
        ApiNotValidException apiNotValidException = new ApiNotValidException(request,exception.getBindingResult());
        return new ResponseEntity<>(apiNotValidException, HttpStatus.BAD_REQUEST);
    }

}
