package com.qhieco.webmapper;

import com.qhieco.request.web.FeekbackRequest;
import com.qhieco.response.data.web.FeedBackData;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 11:54
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface FeekBackMapper {

    public List<FeedBackData> queryFeekbackListByCondition(HashMap params);

    public int queryFeekbackCountByCodition(HashMap params);

    public List<FeedBackData> queryFeedbackListByConditionExcel(FeekbackRequest feekbackRequest);
}
