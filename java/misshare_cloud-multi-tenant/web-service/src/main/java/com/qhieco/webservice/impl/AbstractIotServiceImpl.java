package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.iotdevice.AbstractIotDevice;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.IotService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.util.TenantHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午3:03
 * <p>
 * 类说明：
 * ${description}
 */
public abstract class AbstractIotServiceImpl<T extends AbstractIotDevice, E extends QueryPaged> implements IotService<T, E> {

    protected String condition;

    /**
     * 注入repository实例
     * @return :IotRepository
     */
    public abstract IotRepository<T>  getDao();

    /**
     * 查询方法
     * @param request
     * @return
     */
    protected abstract Specification<T> where(E request);

    /**
     * 未绑定设备列表查询
     * @param request
     * @return
     */
    protected abstract Specification<T> unbindWhere(E request);

    /**
     * 数据业务属性字段填充
     * @param data
     */
    protected abstract void transientProperty(T data);

    /**
     * excel导出字段补充
     * @param map
     */
    protected abstract void reverseLookup(Map<String, Object> map);

    @Override
    @EnableTenantFilter
    public Resp save(T data) {
        if(condition!=null) {
            try {
                if (!PropertyCheck.uniqueCheck(data, condition)){
                    return RespUtil.errorResp(Status.WebErr.PROPERTY_EXISTS.getCode(),
                            "门禁名称或蓝牙名称已存在");
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(data == null){
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg());
        }
        if (data.getId() != null){
            BeanUtil.converJavaBean(getDao().findOne(data.getId()), data);
        }
        if (data.getUpdateTime() == null) {
            data.setUpdateTime(System.currentTimeMillis());
        }
        if (data.getState() == null) {
            data.setState(Status.Common.VALID.getInt());
        }
        T iot =  getDao().save(data);
        if (iot == null){
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(),Status.WebErr.SYSTEM_ERROR.getMsg());
        }
        return RespUtil.successResp();
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp<AbstractPaged<T>> query(E request){
        // fetch and handle pageable data
        Page<T> page = getDao().findAll(
                where(request),
                PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        List<T> dataList = page.getContent();
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
        UserWebRespository userWebWebRespository = context.getBean(UserWebRespository.class);
        for (T adata:dataList
             ) {
            TenantHelper.fillTenantName(adata);
            transientProperty(adata);
        }
        Integer count = (int) page.getTotalElements();
        AbstractPaged<T> data = AbstractPaged.<T>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(dataList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public Resp unbindQuery(E request) {
        Page<T> page = getDao().findAll(unbindWhere(request),PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        List<T> dataList = page.getContent();
        Integer count = (int) page.getTotalElements();
        AbstractPaged<T> data = AbstractPaged.<T>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(dataList).build();
        return RespUtil.successResp(data);
    }

    @Override
    @EnableTenantFilter
    public Resp<T> one(Integer id){
        T data = getDao().findOne(id);
        if (data == null) {
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg());
        }
        transientProperty(data);
        return RespUtil.successResp(data);
    }

    @Override
    @EnableTenantFilter
    public Resp excel(E request, Class cls, OutputStream outputStream) throws IOException {
        List<T> dataList = getDao().findAll(
                where(request));
        if (dataList.size() == 0){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for (T adata:dataList
                ) {
            transientProperty(adata);
        }
        List<Map<String, Object>> mapList = ExcelUtil.dataToMap(dataList, cls);
        for (Map<String, Object> map : mapList
                ) {
            reverseLookup(map);
        }
        ExcelUtil.write(cls.getSimpleName(), mapList, outputStream);
        return RespUtil.successResp();
    }
    
    public Resp bind(Integer[] entityIds, Integer targetId){
        for (Integer entityId:entityIds
                ) {
            T entity = getDao().findOne(entityId);
            if (entity != null){
                entity.setForeignKey(targetId);
                getDao().save(entity);
            }
        }
        return RespUtil.successResp();
    }

    public Resp unbind(Integer entityId) {
        T entity = getDao().findOne(entityId);
        if (entity != null){
            entity.setForeignKey(null);
            getDao().save(entity);
        }
        return RespUtil.successResp();
    }
}
