package com.qhieco.webbitem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.util.LogUrl;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static net.logstash.logback.argument.StructuredArguments.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:29
 * <p>
 * 类说明：
 * 采用Spring AOP方式记录请求日志
 */
@Aspect
@Component
@Slf4j
public class WebRequestLogAspect {

    private static final String PREFIX = "user";
    private static final String POST_METHOD = "post";
    private static final String pattern = "/web1.2/(.*)";
    private static final String HTTP_METHOD_POST = "POST";

    private ThreadLocal<Map<String, Object>> tLocal = new ThreadLocal<>();

//    @Autowired
//    LogOperationWebRepository operationWebRepository;

//    @Autowired
//    private OptLogService optLogService;

    @Pointcut("execution(public * com.qhieco.web.controller.*.*(..))")
    public void webRequestLog() {
    }

    @Before("webRequestLog()")
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
            String params = "";
            Map<String, String[]> reqMap = request.getParameterMap();
            StringJoiner joiner = new StringJoiner(",");
            reqMap.forEach((k, v) -> {
                if (v.length != 1) {
                    joiner.add(k + ":" + Arrays.toString(v));
                } else {
                    joiner.add(k + ":" + v[0]);
                }
            });
            params = joiner.toString();
            Map<String, Object> myMap = new HashMap<>();
            myMap.put("url", uri);
            myMap.put("httpMethod", httpMethod);
            myMap.put("params", params);
            myMap.put("sessionId", sessionId);
            myMap.put("startTime", System.currentTimeMillis());
            myMap.put("remoteIp", remoteAddr);
            tLocal.set(myMap);
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            MDC.put("request_context", gson.toJson(myMap));
        } catch (Exception e) {
            log.error("*******操作请求日志记录失败doBefore()*******", e);
        }
    }

    @AfterReturning(returning = "request", pointcut = "webRequestLog()")
    public void doAfterReturning(Object request) {
        try {
            MDC.clear();
            Map<String, Object> myMap = tLocal.get();
            tLocal.remove();
            Long requestTime = (System.currentTimeMillis() - (Long) myMap.get("startTime"));
            myMap.put("totalTime", requestTime);
            myMap.remove("startTime");
            log.info("log message {}", entries(myMap));
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
        for (Object paramElement : paramsArray) {
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

    @AfterReturning(returning = "result", pointcut = "execution(public * com.qhieco.web.controller.*.*(..))")
    public void logBefore(Object result) {
        try {
            Resp resp;
            if (result instanceof Resp) {
                resp = (Resp) result;
                if (resp.getError_code() != Status.WebErr.SUCCESS.getCode()) {
                    return;
                }
            } else {
                return;
            }
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest httpServletRequest = sra.getRequest();
            UserWeb admin = (UserWeb) httpServletRequest.getSession().getAttribute(PREFIX);
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(httpServletRequest.getRequestURL());
            if (m.find() && httpServletRequest.getMethod().equalsIgnoreCase(POST_METHOD)) {
                String opration = LogUrl.logAction(m.group(1), httpServletRequest);
                if (opration != null && admin != null) {
//                    LogOperationWeb logAdmin = new LogOperationWeb();
//                    logAdmin.setOperateTime(System.currentTimeMillis());
//                    logAdmin.setContent(opration);
//                    logAdmin.setSourceIp(httpServletRequest.getRemoteAddr());
//                    logAdmin.setWebUserId(admin.getId());
//                    operationWebRepository.save(logAdmin);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
