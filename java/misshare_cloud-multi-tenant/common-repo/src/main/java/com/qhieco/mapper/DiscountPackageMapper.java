package com.qhieco.mapper;

import com.qhieco.request.api.UserPackageRequest;
import com.qhieco.response.data.api.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 10:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface DiscountPackageMapper {
    /**
     * 根据小区获取绑定套餐
     * @return
     */
    DiscountPackageData findParklotPackageByParkId(Integer parklotId);


    /**
     * 获取小区绑定套餐信息
     * @param parklotId
     * @return
     */
    DiscountPackageData getDiscountPackageInfo(Integer parklotId);

    /**
     * 根据套餐包id查询时间列表
     * @param packageId
     * @return
     */
    List<DiscountRuleTimeData> findRuleTimeList(Integer packageId);

    /**
     * 查询改套餐下的规格
     * @param packageId
     * @return
     */
    List<PackageFormatSumData> findPackageFormatSum(Integer packageId);

    /**
     * 查询停车场名称
     * @param packageId
     * @return
     */
    List<String> findPackageByParklotName(Integer packageId);

    /**
     * 查询该车牌是否在该小区购买过套餐
     * @param userPackageRequest
     * @return
     */
    UserPackageData findUserPackage(UserPackageRequest userPackageRequest);

    /**
     * 查询小区套餐购买的数量
     * @param userPackageRequest
     * @return
     */
    Integer findUserPackageByCount(UserPackageRequest userPackageRequest) ;

    List<DiscountPackageListData> queryPackageListByPlateId(@Param("plateIds") List<Integer> plateIds);

    List<DiscountPackageListData.ParklotInfo> queryPackageParklotInfoByPackageId(@Param("packageId") Integer packageId);

    List<DiscountPackageListData.TimeRule> queryPackageTimeRuleByPackageId(@Param("packageId") Integer packageId);
}

