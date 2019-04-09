package com.qhieco.webmapper;

import com.qhieco.request.web.LogOperationWebRequest;
import com.qhieco.response.data.web.LogOperationWebData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 16:02
 * <p>
 * 类说明：
 * ${description}
 */
@Mapper
public interface LogOperateWebMapper {

    public List<LogOperationWebData> queryOperationWebList(LogOperationWebRequest logOperationWebRequest);

    public Integer queryOperationWebCount(LogOperationWebRequest logOperationWebRequest);

    public List<LogOperationWebData> operationWebListExcel(LogOperationWebRequest logOperationWebRequest);
}
