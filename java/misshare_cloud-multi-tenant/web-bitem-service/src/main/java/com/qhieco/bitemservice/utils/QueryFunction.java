package com.qhieco.bitemservice.utils;

import com.qhieco.constant.Status;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.exception.ExcelException;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-26 下午8:44
 * <p>
 * 类说明：
 * 通用的查询方法工具
 */
public class QueryFunction<T, E extends QueryPaged> {

    @Setter
    protected Function<T,T> transientProperty;
    @Setter
    protected Consumer<Map<String, Object>> reverseLookup;

    protected JpaSpecificationExecutor<T> repository;

    public QueryFunction(JpaSpecificationExecutor<T> dao){
         this.repository = dao;
    }

    /**
     * 公用列表查询方法
     * @param request　查询变量
     * @param where　查询添加Specification
     * @return
     */
    public Resp<AbstractPaged<T>> query(E request, Specification<T> where){
        // fetch and handle pageable data
        Page<T> page = repository.findAll(
                where,
                PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        List<T> dataList = page.getContent();
        if (transientProperty != null) {
            List<T> newDataList = new ArrayList<>();
            dataList.forEach(data -> newDataList.add(transientProperty.apply(data)));
            dataList = newDataList;
        }
        Integer count = (int) page.getTotalElements();
        AbstractPaged<T> data = AbstractPaged.<T>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(dataList).build();
        return RespUtil.successResp(data);
    }

    /**
     * 公用列表查询方法
     * @param request　查询变量
     * @param where　查询添加Specification
     * @return
     */
    public Resp<AbstractPaged<T>> queryOrder(E request, Specification<T> where,Sort sort){
        // fetch and handle pageable data
        Page<T> page = repository.findAll(
                where,
                PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        page.getSort().and(sort);
        List<T> dataList = page.getContent();
        List<T> newDataList = new ArrayList<>();
        if (transientProperty != null) {
            dataList.forEach(data -> newDataList.add(transientProperty.apply(data)));
        }
        Integer count = (int) page.getTotalElements();
        AbstractPaged<T> data = AbstractPaged.<T>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(newDataList).build();
        return RespUtil.successResp(data);
    }

    /**
     * 公用excel导出方法
     * @param where 查询添加
     * @param cls　导出对象类
     * @param outputStream　输出文件流
     * @return
     * @throws IOException
     */
    public Resp excel(Specification<T> where, Class cls, OutputStream outputStream) throws IOException {
        List<T> dataList = repository.findAll(
                where);
        if (dataList.size() == 0){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        List<T> newDataList = new ArrayList<>();
        if (transientProperty != null) {
            dataList.forEach(data -> newDataList.add(transientProperty.apply(data)));
        }
        List<Map<String, Object>> mapList = ExcelUtil.dataToMap(newDataList, cls);
        if(reverseLookup != null) {
            for (Map<String, Object> map : mapList
                    ) {
                reverseLookup.accept(map);
            }
        }
        ExcelUtil.write(cls.getSimpleName(), mapList, outputStream);
        return RespUtil.successResp();
    }

    public static <T, E extends QueryPaged> Resp<AbstractPaged<T>> query(E request, Specification<T> where, JpaSpecificationExecutor<T> repository){
        // fetch and handle pageable data
        Page<T> page = repository.findAll(
                where,
                PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        List<T> dataList = page.getContent();
        Integer count = (int) page.getTotalElements();
        AbstractPaged<T> data = AbstractPaged.<T>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(dataList).build();
        return RespUtil.successResp(data);
    }
}
