package com.qhieco.webmapper;

import com.qhieco.request.web.LoginLogRequest;
import com.qhieco.response.data.web.LogLoginData;
import org.apache.ibatis.annotations.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 15:32
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface LogLoginMapper {

    public List<LogLoginData> queryLogLoginListByCondition(HashMap<String, Object> params);

    public int queryLogLoginCount(HashMap<String, Object> params);

    public  List<LogLoginData> queryLogLoginListExcel(HashMap<String, Object> params);
}
