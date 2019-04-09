package com.qhieco.util;

import com.qhieco.response.data.api.ReserveOrderRespData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/12 18:10
 * <p>
 * 类说明：
 * ${说明}
 */
public class SortUtil {

    /**
     * 对预约列表进行倒序排列
     *
     * @param reserveOrderList
     * @return
     */
    public static List<ReserveOrderRespData> sortReserveOrderList(List<ReserveOrderRespData> reserveOrderList) {
        Collections.sort(reserveOrderList, new Comparator<ReserveOrderRespData>() {
            @Override
            public int compare(ReserveOrderRespData list1, ReserveOrderRespData list2) {
                return list2.getCreateTime().compareTo(list1.getCreateTime());
            }
        });
        return reserveOrderList;
    }
}
