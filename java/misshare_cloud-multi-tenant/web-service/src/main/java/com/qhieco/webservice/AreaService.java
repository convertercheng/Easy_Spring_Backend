package com.qhieco.webservice;

import com.qhieco.commonentity.Area;
import com.qhieco.request.web.AreaRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AreaData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午10:48
 *          <p>
 *          类说明：
 *          地区Service
 */
public interface AreaService {
    /**
     * 新增或编辑地区
     * @param area
     * @return
     */
    Resp save(Area area);

    /**
     * 查询地区列表
     * @param areaRequest
     * @return
     */
    Resp children(AreaRequest areaRequest);

    /**
     * 获取地区
     * @param parentId 地区上级id
     * @return
     */
    List<AreaData> getAreas(Integer parentId);

    /**
     * 查询地区及其父区域构成的地区列表
     * @param areaId
     * @return
     */
    List<Area> findAreaList(Integer areaId);
}
