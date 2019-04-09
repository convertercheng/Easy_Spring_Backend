package com.qhieco.apiread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qhieco.apiservice.OperateLogService;
import com.qhieco.commonentity.LogRequestApp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static net.logstash.logback.argument.StructuredArguments.entries;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:29
 * <p>
 * 类说明：
 *     采用Spring AOP方式记录请求日志
 */
@Aspect
@Component
@Slf4j
public class AppRequestLogAspect {

    private ThreadLocal<Map<String, Object>> tLocal = new ThreadLocal<>();

    private ThreadLocal<LogRequestApp> tLocalRequest = new ThreadLocal<>();

    @Autowired
    private OperateLogService operateLogService;

    @Pointcut("execution(public * com.qhieco.apiread.web.*.*(..))")
    public void appRequestLog() {}

    @Before("appRequestLog()")
    public void deBefore(JoinPoint joinPoint) {
        try {
            long beginTime = System.currentTimeMillis();

            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String path = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String sessionId = request.getSession().getId();
            String httpMethod = request.getMethod();
            Map<String, String[]> reqMap = request.getParameterMap();
            StringJoiner joiner = new StringJoiner(",");
            reqMap.forEach((k,v) -> {
                if(v.length != 1) {
                    joiner.add(k + ":" + Arrays.toString(v));
                }else {
                    joiner.add(k + ":" + v[0]);
                }
            });
            String params = joiner.toString();
            Map<String, Object> myMap = new HashMap<>(16);
            myMap.put("url", uri);
            myMap.put("httpMethod", httpMethod);
            myMap.put("params",params);
            myMap.put("sessionId",sessionId);
            myMap.put("startTime",System.currentTimeMillis());
            myMap.put("remoteIp",remoteAddr);
            tLocal.set(myMap);
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                    .create();
            MDC.put("request_context",gson.toJson(myMap));
            LogRequestApp logRequestApp = new LogRequestApp();
            logRequestApp.setMethod(httpMethod);
            logRequestApp.setUri(uri);
            tLocalRequest.set(logRequestApp);
        } catch (Exception e) {
            log.error("*******操作请求日志记录失败doBefore()*******", e);
        }
    }

    @AfterReturning(returning = "request", pointcut = "appRequestLog()")
    public void doAfterReturning(Object request) {
        try {
            MDC.clear();
            Map<String, Object> myMap = tLocal.get();
            tLocal.remove();
            Long requestTime = (System.currentTimeMillis() - (Long) myMap.get("startTime"));
            myMap.put("totalTime",requestTime);
            myMap.remove("startTime");
            log.info("log message {}", entries(myMap));
            LogRequestApp logRequestApp = tLocalRequest.get();
            logRequestApp.setSuccessTimes(1);
            logRequestApp.setTimes(1);
            tLocalRequest.remove();
            operateLogService.saveLog(logRequestApp);
        } catch (Exception e) {
            log.error("***操作请求日志记录失败doAfterReturning()***", e);
        }
    }

    /**
     * 请求参数拼装
     *
     * @param paramsArray 参数数组
     * @return 拼接后的请求参数
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        for (Object paramElement: paramsArray) {
            params.append(paramElement.toString());
        }
        return params.toString().trim();
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
