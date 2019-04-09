package com.qhieco.webmapper;

import com.qhieco.request.web.DiscountPackageRequest;
import com.qhieco.response.data.web.DiscountFormatSumData;
import com.qhieco.response.data.web.DiscountPackageData;
import com.qhieco.response.data.web.DiscountPackageStaticData;
import com.qhieco.response.data.web.DiscountRuleTimeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 分页查询套餐信息
     *
     * @param request
     * @return
     */
    List<DiscountPackageData> pageDiscountPackage(DiscountPackageRequest request);
    /**
     * 查询套餐信息总记录数
     *
     * @param request
     * @return
     */
    Integer pageDiscountPackageTotalCount(DiscountPackageRequest request);

    /**
     * 分页查询套餐统计信息
     * @param request
     * @return
     */
    List<DiscountPackageData> pagePackageStatic(DiscountPackageRequest request);

    /**
     * 查询套餐统计总记录数
     * @param request
     * @return
     */
    Integer pagePackageStaticTotalCount(DiscountPackageRequest request);

    /**
     * 获取套餐详细
     * @param request
     * @return
     */
    DiscountPackageData findPackageDetailed(DiscountPackageRequest request);

    /**
     * 根据套餐获取时段详细列表
     * @param request
     * @return
     */
    List<DiscountRuleTimeData> findRuleTimeList(DiscountPackageRequest request);

    /**
     * 根据套餐获取规格列表
     * @param request
     * @return
     */
    List<DiscountFormatSumData> findFormatSumList(DiscountPackageRequest request);

    /**
     * 新增套餐详细
     * @param request
     * @return
     */
    int insertDiscountPackageData(DiscountPackageRequest request);

    /**
     * 修改套餐详细
     * @param request
     * @return
     */
    int updateDiscountPackageData(DiscountPackageRequest request);

    /**
     * 修改套餐展示状态
     * @param request
     * @return
     */
    int updateParklotState(DiscountPackageRequest request);

    /**
     * 修改时间段状态为禁用
     * @param request
     * @return
     */
    int updateRuleTimeState(DiscountPackageRequest request);

    /**
     * 根据套餐新增时段
     * @param request
     * @return
     */
    int insertRuleTimeData(DiscountPackageRequest request);

    /**
     * 导出套餐列表
     * @param request
     * @return
     */
    List<DiscountPackageData> excelPackage(DiscountPackageRequest request);

    /**
     * 根据小区获取绑定套餐
     * @return
     */
    DiscountPackageData findParklotPackageByParkId(Integer parklotId);

    /**
     * 删除小区套餐关联关系
     * @return
     */
    int delParklotByPackage(@Param("parklotId") Integer parklotId, @Param("packageId") Integer packageId);

    /**
     * 保存小区套餐关联关系
     * @return
     */
    int saveParklotByPackage(@Param("parklotId") Integer parklotId, @Param("packageId") Integer packageId, @Param("packageState") Integer packageState);

    /**
     * 导出套餐统计列表
     * @param request
     * @return
     */
    List<DiscountPackageStaticData> excelStaticPackage(DiscountPackageRequest request);

    /**
     * 保存套餐关联规格关系
     * @param daytime,sumNumber,packageId
     */
    void insertFormatSumData(@Param("daytime") String daytime, @Param("sumNumber") String sumNumber, @Param("packageId") Integer packageId);

    void delFormatSumData(Integer packageId);
}

