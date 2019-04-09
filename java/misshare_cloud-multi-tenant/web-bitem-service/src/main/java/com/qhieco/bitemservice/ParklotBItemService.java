package com.qhieco.bitemservice;


import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.ParkLotData;

import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/4 15:05
 * <p>
 * 类说明：
 * ${description}
 */
public interface ParklotBItemService {

    Resp<List<ParkLotData>> findAllUsableParklot(ParklotRequest request);

    Resp<List<ParkLotData>> findByUserAllParklot(ParklotRequest request);

    Resp<List<ParkLotData>> findParklotAdminByPage(ParklotRequest request);

    Resp<ParkLotData> findAdminUserDetailed(ParklotRequest request);

    Resp updateAdminUserDetailed(ParklotRequest request);
}
