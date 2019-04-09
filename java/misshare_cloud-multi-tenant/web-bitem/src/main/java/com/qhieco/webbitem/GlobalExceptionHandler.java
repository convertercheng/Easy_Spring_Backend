package com.qhieco.webbitem;

import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/23 下午2:36
 * <p>
 * 类说明：
 *     统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Resp handleException(Exception e, HttpServletResponse response) throws Exception {
        if (e instanceof QhieWebException) {
            QhieWebException qhieException = (QhieWebException) e;
            return RespUtil.errorResp(qhieException.getCode(), qhieException.getMessage());
        } else if (e instanceof ParamException) {
            ParamException paramException = (ParamException) e;
            return RespUtil.errorResp(paramException.getCode(), paramException.getMessage());
        } else if (e instanceof ExcelException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return RespUtil.errorResp(((ExcelException) e).getCode(), e.getMessage());
        } else {
//            return RespUtil.errorResp(Status.ApiErr.UNKNOWN_ERROR.getCode(), Status.ApiErr.UNKNOWN_ERROR.getMsg());
            throw e;
        }
    }

}
