package com.qhieco.util;

import com.qhieco.commonentity.Role;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-6 下午5:07
 * <p>
 * 类说明：
 *     参数检测工具类
 */
@Slf4j
public class ParamCheck {

    private static final String ERROR_MSG_PREFIX = "这些参数不可为空: ";
    private static final String DELIMITER_COMMA = " , ";
    private static final String BASIC_CLASS = "java.lang.object";

    /**
     * 检查参数是否合法
     * @param obj 请求类
     * @param params 传入参数
     * @param <T> 返回类型
     * @return 返回值
     */
    public static <T> Resp check(T obj, String... params){
        Class cls = obj.getClass();
        List<String> emptyInput = new ArrayList<>();
        Map<String, Field> fieldMap = getField(cls);
        try {
            for (String paramName: params) {
                Field clsField = fieldMap.get(paramName);
                if(clsField == null) {
                    continue;
                }
                clsField.setAccessible(true);
                Object value = clsField.get(obj);
                checkValue(emptyInput, paramName, value);
            }
        }catch ( IllegalAccessException e) {
            e.printStackTrace();
        }
        return getResp(emptyInput);
    }

    /**
     * 递归查找父类属性
     * @param cls 类变量
     * @return 返回结果HashMap<String, Field>
     */
    public static HashMap<String, Field> getField(Class<?> cls){
        if (cls == null || cls.getName().toLowerCase().equals(BASIC_CLASS)){
            return new HashMap<>();
        }
        HashMap<String, Field> fieldHashMap= getField(cls.getSuperclass());
        Field[] fieldList = cls.getDeclaredFields();
        for (Field field:fieldList) {
            fieldHashMap.put(field.getName(),field);
        }
        getField(cls.getSuperclass());
        return fieldHashMap;
    }

    /**
     * 返回检查结果
     * @param emptyInput 空输入list
     * @return 返回结果
     */
    private static Resp getResp(List<String> emptyInput) {
        if(emptyInput.size() == Constants.EMPTY_CAPACITY){
            return RespUtil.errorResp(
                    Status.Common.VALID.getInt(),
                    null
            );
        }else{
            return RespUtil.errorResp(
                    Status.Common.INVALID.getInt(),
                    ERROR_MSG_PREFIX + emptyInput.stream().collect(Collectors.joining(DELIMITER_COMMA))
            );
        }
    }

    /**
     * 检测对象中域值是否为null或空
     * @param emptyInput 空输入list
     * @param paramName 参数名称
     * @param value 对象中域值
     */
    private static void checkValue(List<String> emptyInput, String paramName, Object value) {
        if(null == value){
            emptyInput.add(paramName);
        }else if (value instanceof String && StringUtils.isEmpty((String)value)){
            emptyInput.add(paramName);
        }
    }

}
