package com.qhieco.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:05
 *          <p>
 *          类说明：
 */
public class EliminateNull {
    public static <T> T eliminate(T src, T ref) throws NoSuchFieldException, SecurityException, IntrospectionException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Field[] srcFields = src.getClass().getDeclaredFields();
        Field[] refFields = ref.getClass().getDeclaredFields();
        int srcLength = srcFields.length, refLength = refFields.length;
        if(srcLength != refLength) {
            return null;
        }
        for(int i = 0; i < srcLength; i++) {
            Field srcField = srcFields[i];
            if(srcField.getName().equals("id")) {
                continue;
            }
            Field refField = refFields[i];
            PropertyDescriptor srcPd = new PropertyDescriptor(srcField.getName(), src.getClass());
            Method srcGetMethod = srcPd.getReadMethod();
            Object srcValue = srcGetMethod.invoke(src);
            if(srcValue == null) {
                PropertyDescriptor refPd = new PropertyDescriptor(refField.getName(), ref.getClass());
                Method refGetMethod = refPd.getReadMethod();
                Object refValue = refGetMethod.invoke(ref);
                if(refValue != null) {
                    Method srcSetMethod = srcPd.getWriteMethod();
                    srcSetMethod.invoke(src, refValue);
                }
            }
        }
        return src;
    }
}
