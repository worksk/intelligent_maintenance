package com.common.parent_model.exception;

import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.common.ReturnData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:28
 * @Description: 全局统一异常处理
 */
@ControllerAdvice
@Slf4j
public class ExceptionResolver {

    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ReturnData ExceptionHandler(BaseException e){
        log.error("catch BaseException for 基础异常: {}", e.getMessage());
        return ReturnData.fail(e.getCode(), e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnData ExceptionHandler(Exception e){
        log.error("catch Exception for 未捕获异常: {}", e.getMessage());
        return ReturnData.fail(CodeEnum.UN_KNOW_ERROR.getCode(), e.getMessage());
    }

}
