package com.qhieco.webbitemmapper;

import com.qhieco.response.data.webbitem.ParkingRecordData;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 15:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ParkingRecordMapper {

    public List<ParkingRecordData> queryParkingRecordListByCondition(HashMap params);

    public int countParkingRecordListByCondition(HashMap params);

}
