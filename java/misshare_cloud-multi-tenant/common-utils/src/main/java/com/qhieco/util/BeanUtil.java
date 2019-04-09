package com.qhieco.util;

import java.lang.reflect.Method;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 9:03
 * <p>
 * 类说明：
 * 使用反射将一个对象的值赋给另一个对象，相同属性名即可完成赋值
 */
public class BeanUtil {

    /**
     * 转换javabean,
     * 将classSource中的属性值赋值给classTarget，如果classTarget属性有值，则不覆盖
     *
     * @param classSource 提供数据的对象
     * @param classTarget 基准类,被赋值对象
     * @Description TODO 前提条件是都有get和set方法，并且有相同的属性名
     * @author myz
     */
    public static void converJavaBean(Object classSource, Object classTarget) {
        try {
            Class<?> clazzTarget = classTarget.getClass();
            Class<?> clazzSource = classSource.getClass();
            // 得到method方法
            Method[] methodTarget = clazzTarget.getMethods();
            Method[] methodSource = clazzSource.getMethods();

            int lengthTarget = methodTarget.length;
            int lengthSource = methodSource.length;
            if (lengthTarget != 0 && lengthSource != 0) {
                // 创建一个get方法数组，专门存放classSource的get方法。
                Method[] get = new Method[lengthSource];
                for (int i = 0, j = 0; i < lengthSource; i++) {
                    if (methodSource[i].getName().indexOf("get") == 0) {
                        get[j] = methodSource[i];
                        ++j;
                    }
                }

                for (int i = 0; i < get.length; i++) {
                    // 数组初始化的长度多于get方法，所以数组后面的部分是null
                    if (get[i] == null) {
                        continue;
                    }
                    // 得到get方法的值，判断时候为null，如果为null则进行下一个循环
                    Object value = get[i].invoke(classSource, new Object[]{});
                    if (null == value) {
                        continue;
                    }
                    // 得到get方法的名称 例如：getXxxx
                    String getName = get[i].getName();
                    // 得到set方法的时候传入的参数类型，就是get方法的返回类型
                    Class<?> paramType = get[i].getReturnType();
                    Method getMethod = null;
                    try {
                        // 判断在classTarget中时候有classSource中的get方法，如果没有则抛异常继续循环
                        getMethod = clazzTarget.getMethod(getName, new Class[]{});
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    // classTarget的get方法不为空并且classTarget中get方法得到的值为空，进行赋值，如果classTarget属性原来有值，则跳过
                    if (null == getMethod || null != getMethod.invoke(classTarget, new Object[]{}))
                        continue;
                    // 通过getName 例如getXxxx 截取后得到Xxxx，然后在前面加上set，就组装成set的方法名
                    String setName = "set" + getName.substring(3);
                    // 得到classTarget的set方法，并调用
                    Method setMethod = clazzTarget.getMethod(setName, paramType);
                    setMethod.invoke(classTarget, value);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 针对converJavaBean（反向操作）： 先获取target的set方法集，根据set方法集获取并设置source方法的值，不管classTarget有没有值都覆盖
     *
     * @param classSource
     * @param classTarget
     * @Description: TODO    前提条件是都有get和set方法，并且有相同的属性名
     * @author myz
     */
    public static void converJavaBeanOptimize(Object classSource, Object classTarget) throws Exception {
        if (classSource == null || classTarget == null)
            return;
        Class<?> clazzTarget = classTarget.getClass();
        Class<?> clazzSource = classSource.getClass();
        // 得到method方法
        Method[] methodTarget = clazzTarget.getMethods();
        Method[] methodSource = clazzSource.getMethods();

        int lengthTarget = methodTarget.length;
        int lengthSource = methodSource.length;
        if (lengthTarget != 0 && lengthSource != 0) {
            // 创建一个set方法数组，专门存放classTarget的set方法。
            Method[] set = new Method[lengthTarget];
            for (int i = 0, j = 0; i < lengthTarget; i++) {
                if (methodTarget[i].getName().indexOf("set") == 0) {
                    set[j] = methodTarget[i];
                    ++j;
                }
            }

            for (int i = 0; i < set.length; i++) {
                // 数组初始化的长度多于set方法，所以数组后面的部分是null
                if (set[i] == null) {
                    continue;
                }
                // ------------------------------
                // 获取set方法名
                String setMethodName = set[i].getName();
                try {
                    // 获取数据源的数据
                    Object value = clazzSource.getMethod("get" + setMethodName.substring(3), new Class[]{})
                            .invoke(classSource, new Object[]{});
                    if (value == null) {
                        continue;
                    }
                    // 赋值
                    set[i].invoke(classTarget, value);
                } catch (Exception e) {
                }
            }
        }
    }

}
