package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.AreaService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Area;
import com.qhieco.commonrepo.AreaRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.AreaGetRequest;
import com.qhieco.response.data.api.AreaGetRepData;
import com.qhieco.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/7 下午12:21
 * <p>
 * 类说明：
 * 地区Service的实现类
 */
@Service
@Slf4j
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Override
    public List<AreaGetRepData> getSupportAreas(AreaGetRequest areaGetRequest) {
        String timestamp = areaGetRequest.getTimestamp();
        if (StringUtils.isEmpty(timestamp) || CommonUtil.isTimeStampInValid(timestamp)) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer userId = areaGetRequest.getUser_id();
        if (null == userMobileRepository.findOne(userId)) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        List<AreaGetRepData> areas = new ArrayList<>();
        List<Area> provinces =
                areaRepository.findByLevelAndParentIdAndState(Constants.AreaLevel.PROVINCE.ordinal(), Constants.AreaLevel.NATION.ordinal(), Status.Common.VALID.getInt());
        AreaGetRepData areaGetRepData;
        List<AreaGetRepData.InferiorBeanX> inferiorBeanXs;
        for (Area province : provinces) {
            areaGetRepData = new AreaGetRepData();
            Integer provinceId = province.getId();
            areaGetRepData.setId(provinceId);
            areaGetRepData.setName(province.getName());
            inferiorBeanXs = new ArrayList<>();
            AreaGetRepData.InferiorBeanX inferiorBeanX;
            List<Area> cities = areaRepository.findByLevelAndParentIdAndState(Constants.AreaLevel.CITY.ordinal(), provinceId, Status.Common.VALID.getInt());
            if (null == cities || Constants.EMPTY_CAPACITY == cities.size()) {
                continue;
            }
            List<AreaGetRepData.InferiorBeanX.InferiorBean> inferiorBeanList;
            for (Area city : cities) {
                inferiorBeanX = new AreaGetRepData.InferiorBeanX();
                Integer cityId = city.getId();
                inferiorBeanX.setId(cityId);
                inferiorBeanX.setName(city.getName());
                inferiorBeanList = new ArrayList<>();
                List<Area> regions = areaRepository.findByLevelAndParentIdAndState(Constants.AreaLevel.REGION.ordinal(), cityId, Status.Common.VALID.getInt());
                if (null == regions || Constants.EMPTY_CAPACITY == regions.size()) {
                    continue;
                }
                AreaGetRepData.InferiorBeanX.InferiorBean inferiorBean;
                for (Area region : regions) {
                    inferiorBean = new AreaGetRepData.InferiorBeanX.InferiorBean();
                    inferiorBean.setId(region.getId());
                    inferiorBean.setName(region.getName());
                    inferiorBeanList.add(inferiorBean);
                }
                inferiorBeanX.setInferior(inferiorBeanList);
                inferiorBeanXs.add(inferiorBeanX);
            }
            areaGetRepData.setInferior(inferiorBeanXs);
            for (AreaGetRepData.InferiorBeanX inferiorBean : areaGetRepData.getInferior()) {
                if (inferiorBean.getInferior().size() != Constants.EMPTY_CAPACITY) {
                    areas.add(areaGetRepData);
                }
            }
        }
        return areas;
    }
}
