package com.rean.shopspring.exception;

import com.rean.shopspring.pojo.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALID_FAIL_MSG = "参数检验不通过";

    //处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    public Result<List<String>> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).toList();
        return Result.error(VALID_FAIL_MSG,collect);
    }

    //  处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
        return Result.error(VALID_FAIL_MSG,collect);
    }

    //  处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<List<String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream().map(o -> o.getMessage()).collect(Collectors.toList());
        return Result.error(VALID_FAIL_MSG,collect);
    }

    // 统一sql异常处理类
    @Autowired
    SqlExceptionHandler sqlExceptionHandler;

    // sql 报错处理
    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus
    public Result sqlError(SQLException e) {
        // 我们交给异常处理类去处理
        return Result.error(sqlExceptionHandler.handle(e));
    }


    // 处理以上处理不了的其他异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "操作失败");
    }
}
