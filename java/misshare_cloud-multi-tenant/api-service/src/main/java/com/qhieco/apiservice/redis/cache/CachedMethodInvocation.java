package com.qhieco.apiservice.redis.cache;


import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 标记了缓存注解的方法类信息
 * 用于主动刷新缓存时调用原始方法加载数据
 *
 * @author yuhao.wang
 */
@Data
public final class CachedMethodInvocation implements Serializable {

    private static final long serialVersionUID = 4053574712960451939L;

    private Object key;
    private String targetBean;
    private String targetMethod;
    private List<Object> arguments;
    private List<String> parameterTypes = new ArrayList<>();
    private Long expirationSecondTime;
    private Long startTime;

    public CachedMethodInvocation() {
    }

    public CachedMethodInvocation(Object key, Object targetBean, Method targetMethod, Class[] parameterTypes, Object[] arguments, Long expirationSecondTime) {
        this.key = key;
        this.targetBean = targetBean.getClass().getName();
        this.targetMethod = targetMethod.getName();
        if (arguments != null && arguments.length != 0) {
            this.arguments = Arrays.asList(arguments);
        }
        if (parameterTypes != null && parameterTypes.length != 0) {
            for (Class clazz : parameterTypes) {
                this.parameterTypes.add(clazz.getName());
            }
        }
        this.expirationSecondTime = expirationSecondTime*1000;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 必须重写equals和hashCode方法，否则放到set集合里没法去重
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CachedMethodInvocation that = (CachedMethodInvocation) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}

