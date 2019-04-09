package com.qhieco.webbitemmapper;

import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.data.web.ParkLotData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/4 15:05
 * <p>
 * 类说明：小区
 * ${说明}
 */
@Mapper
public interface ParklotBItemMapper {

    /**
     * 根据条件获取所有可用的小区列表
     * @param request
     * @return
     */
    List<ParkLotData> findAllUsableParklot(ParklotRequest request);

    /**
     * 获取分配小区的所有列表
     * @param request
     * @return
     */
    List<ParkLotData> findByUserAllParklot(ParklotRequest request);
    /**
     * 根据用户ID删除小区关联
     * @param userId
     */
    void delRelationByUserId(Integer userId);

    /**
     * 更新小区关联
     *
     * @param request
     */
    void insert(ParklotRequest request);

    /**
     * 分页获取小区和停车场管理员信息
     * @param request
     * @return
     */
    List<ParkLotData> findParklotAdminByPage(ParklotRequest request);

    /**
     * 小区管理员信息查询总数
     * @param request
     * @return
     */
    Integer pageAdminUserTotalCount(ParklotRequest request);

    /**
     * 判断用户是否是业主身份
     * @param request
     * @return
     */
    ParkLotData findAdminUserDetailed(ParklotRequest request);

    /**
     * 获取用户类型与小区关联数据
     * @param request
     * @return
     */
    ParkLotData findAdminUserIsExistByParklot(ParklotRequest request);

    /**
     *判断用户是否存在可开票余额
     * @param request
     * @return
     */
    List<ParkLotData> findAdminUserByApply(ParklotRequest request);

    /**
     * 判断用户是否存在未完成订单
     * @param request
     * @return
     */
    List<ParkLotData> findAdminByOrder(ParklotRequest request);

    /**
     * 更新小区管理员信息
     * @param request
     * @return
     */
    Integer updateAdminUserDetailed(ParklotRequest request);
}
