package com.qhieco.commonrepo;
import com.qhieco.commonentity.ParklotDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/7/6 10:34
 * <p>
 * 类说明：
 *      停车场区域数据库交互层
 */
public interface ParklotDistrictRepository extends JpaRepository<ParklotDistrict, Integer>,JpaSpecificationExecutor<ParklotDistrict> {

    /**
     * 查询区域名称是否存在
     * @param districtName parklotId  state
     * @return ParklotDistrict
     */
    ParklotDistrict findParklotDistrictByDistrictNameAndParklotIdAndState(String districtName, Integer parklotId, Integer state);

    /**
     * 查询修改的区域名称是否存在
     * @param districtName
     * @param parklotId
     * @param state
     * @param id
     * @return
     */
    @Query("select p from ParklotDistrict p where p.districtName=?1 and p.parklotId=?2 and p.state=?3 and p.id <>?4 ")
    ParklotDistrict findParklotDistrictByDistrictByName(String districtName, Integer parklotId, Integer state, Integer id);

    /**
     * 查看区域详情信息
     * @param id
     * @param state
     * @return
     */
    ParklotDistrict findByIdAndState(Integer id, Integer state);

    /**
     * 根据停车场ID查询区域列表
     * @param parklotId
     * @param state
     * @return
     */
    List<ParklotDistrict> getDistinctByParklotIdAndState(Integer parklotId, Integer state);
}
