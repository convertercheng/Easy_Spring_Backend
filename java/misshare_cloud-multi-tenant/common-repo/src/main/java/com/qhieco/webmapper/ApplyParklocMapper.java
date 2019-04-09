package com.qhieco.webmapper;

import com.qhieco.commonentity.ApplyParkloc;
import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.response.data.web.ApplyParklocData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 11:43
 * <p>
 * 类说明：
 * 申请工位工单列表Mapper
 */
@Mapper
public interface ApplyParklocMapper {

    /**
     * 分页显示申请工单列表
     * @param applyParklocRequest
     * @return
     */
    List<ApplyParklocData> pageApply(ApplyParklocRequest applyParklocRequest);

    /**
     * execl导出申请工单列表
     * @param applyParklocRequest
     * @return
     */
    List<ApplyParklocData> excelApply(ApplyParklocRequest applyParklocRequest);

    /**
     * 申请工单列表记录数
     * @param applyParklocRequest
     * @return
     */
    Integer pageApplyTotalCount(ApplyParklocRequest applyParklocRequest);
}
