package com.qhieco.webmapper;

import com.qhieco.commonentity.Operator;
import com.qhieco.commonentity.relational.ParklotParamsB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 15:30
 * <p>
 * 类说明：
 * ${description}
 */
@Mapper
public interface ParklotParamsMapper {

    /**
     * 查询停车场参数列表
     * @param parklotId
     * @param state
     * @param groupName
     * @return
     */
    List<ParklotParamsB> queryParklotParams(@Param("parklotId") Integer parklotId, @Param("state") Integer state,
                                            @Param("groupName") String groupName);

    /**
     * 根据主键ID查询运营商信息
     * @param id
     * @param state
     * @return
     */
    Operator findOperatorById(@Param("id") Integer id, @Param("state") Integer state);

    @Select(value = "SELECT qhvalue FROM b_parklot_params WHERE parklot_id=#{parklotId} AND qhkey=#{key} AND state=1 LIMIT 1")
    public String queryParklotParamsValue(@Param("parklotId") Integer parklotId, @Param("key") String key);

}

