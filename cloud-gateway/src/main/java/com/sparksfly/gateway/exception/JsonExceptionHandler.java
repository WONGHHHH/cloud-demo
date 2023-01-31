package com.sparksfly.gateway.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理
 *
 * <p>异常时用JSON代替HTML异常信息<p>
 *
 * @author wh
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        Throwable error = super.getError(request);
        /* 处理全局自定义异常类拦截 */
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            code = HttpStatus.NOT_FOUND.value();
        }
        if (error instanceof org.springframework.web.server.ResponseStatusException) {
            code = HttpStatus.NOT_FOUND.value();
        }
        return response(code, code, this.buildMessage(request, error));
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 错误属性
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes 错误属性
     * @return httpStatus
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("http-code");
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return httpStatus.value();
    }

    /**
     * 构建异常信息
     *
     * @param request 请求域
     * @param ex      错误类
     * @return String
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    /**
     * 构建返回的JSON数据格式
     *
     * @param status       状态码
     * @param errorMessage 异常信息
     * @return Map
     */
    public static Map<String, Object> response(int httpCode, int status, String errorMessage) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("http-code", httpCode);
        map.put("code", status);
        map.put("message", errorMessage);
        map.put("data", null);
        return map;
    }

}
