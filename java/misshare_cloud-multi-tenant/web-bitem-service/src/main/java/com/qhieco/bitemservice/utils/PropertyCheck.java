package com.qhieco.bitemservice.utils;

import com.qhieco.commonentity.Area;
import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.constant.Status;
import com.qhieco.request.web.PropertyQuery;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.PropertyCheckMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-12 下午7:01
 * <p>
 * 类说明：
 * 对象属性冗余性检验工具类
 */
@Slf4j
public class PropertyCheck {

    /**
     * 对象属性唯一性校验方法
     * @param property 变量属性名
     * @param data　对象实例
     * @param repository　对象相应的仓库类
     * @param <T>　commonEntity泛型
     * @param <E> Jpa仓库泛型
     * @return
     */
    public static <T, E extends JpaRepository<T, Integer>> Boolean uniqueCheck(String property, T data, E repository) {
        Class beanCls = repository.getClass();
        Method[] clsMethods = beanCls.getMethods();
        Method targetMethod = null;
        for (Method m:clsMethods
             ) {
            if (m.getName().equals("findBy"+property.substring(0, 1).toUpperCase() + property.substring(1))){
                targetMethod = m;
            }
        }
        Resp paramResp = ParamCheck.check(data, property);
        Class dataClass = data.getClass();
        Field[] dataFileds = dataClass.getDeclaredFields();
        Field targetProperty = null;
        Field idProperty = null;
        for (Field filed:dataFileds
             ) {
            if(filed.getName().equals(property)){
                targetProperty = filed;
                targetProperty.setAccessible(true);
            }

            if("id".equals(filed.getName())){
                idProperty = filed;
                idProperty.setAccessible(true);
            }
        }
        if (idProperty == null || targetProperty == null || targetMethod==null){
            return false;
        }
        try {
            if (idProperty.get(data) == null){
                if (paramResp.getError_code().equals(Status.Common.INVALID.getInt())){
                    RespUtil.errorResp(Status.Common.INVALID.getInt(),Status.Common.INVALID.getMsg());
                }
                String targetValue = (String) targetProperty.get(data);
                if (targetMethod.invoke(repository, targetValue) != null){
                    return false;
                }
            }else{
                if (paramResp.getError_code().equals(Status.Common.VALID.getInt())){
                    String targetValue = (String) targetProperty.get(data);
                    T dataOld = (T) targetMethod.invoke(repository, targetValue);
                    log.info("oldData {}",dataOld);
                    if (dataOld!=null){
                        Object oldId = idProperty.get(dataOld);
                        Object id = idProperty.get(data);
                        if(!oldId.equals(id)) {
                            return false;
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Boolean uniqueCheck(Object data, String condition) throws NoSuchFieldException, IllegalAccessException {
        Class cls = data.getClass();
        val fields = ParamCheck.getField(cls);
        Pattern fieldPattern= compile("\\w+");
        List<String> placeHolder = new ArrayList<>();
        Matcher match = fieldPattern.matcher(condition);
        while (match.find()){
            val name = match.group();
            val field = fields.get(name);
            val columnAnno = field.getAnnotation(Column.class);
            StringBuilder clause = new StringBuilder();
            if(columnAnno !=null && !"".equals(columnAnno.name())){
                clause.append(columnAnno.name());
            }else {
                clause.append(convert(field.getName()));
            }
            field.setAccessible(true);
            clause.append("=");
            Object value = field.get(data);
            if(value!=null && value.getClass()==String.class){
                value = "\'"+value+"\'";
            }
            clause.append(value);
            placeHolder.add(clause.toString());
        }
        String format = condition.replaceAll("\\w+", "@");
        format = format.replaceAll("&"," AND ");
        format = format.replaceAll("\\|"," OR ");
        val whereClause = new StringBuilder();
        final int[] loc = {0};
        format.chars().mapToObj(c ->(char) c).forEach(character ->
                {
                    if (character=='@'){
                        whereClause.append(placeHolder.get(loc[0]++));
                    }else {
                        whereClause.append(character);
                    }
                }
        );
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
        PropertyCheckMapper checkMapper = context.getBean(PropertyCheckMapper.class);
        Table tableAnno = (Table) cls.getAnnotation(Table.class);
        if(tableAnno == null){
            return false;
        }
        PropertyQuery query = new PropertyQuery();
        query.setTableName(tableAnno.name());
        query.setWhereClause(whereClause.toString());
        val idSet = checkMapper.uniqueCheck(query);
        val idField = fields.get("id");
        idField.setAccessible(true);
//        if(idField.get(data)==null){
//            return idSet == null || idSet.size() == 0;
//        }else {
//            if (idSet == null || idSet.size() == 0){
//                return true;
//            }
//            Integer id = (Integer) idField.get(data);
//            return idSet.contains(id);
//        }
        if (idSet == null || idSet.size() == 0){
            return true;
        }
        if (idField.get(data)!=null){
            Integer id = (Integer) idField.get(data);
            return (idSet.contains(id) && idSet.size()==1);
        }else {
            return false;
        }
    }

    private static String convert(String name){
        StringBuilder stringBuilder = new StringBuilder();
        name.chars().forEach(character -> {
            if(65<=character && character <= 90){
                stringBuilder.append("_");
                stringBuilder.append((char)(character+32));
            }else {
                stringBuilder.append((char) character);
            }

        });
        return stringBuilder.toString();
    }

}
