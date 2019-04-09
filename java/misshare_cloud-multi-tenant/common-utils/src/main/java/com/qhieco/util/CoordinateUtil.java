package com.qhieco.util;

import com.qhieco.constant.Constants;
import com.qhieco.map.Coordinate;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 17:02
 * <p>
 * 类说明：
 *  坐标计算工具类
 */
public class CoordinateUtil {

    /**
     * 计算两点之间的距离
     * @param start 起始点
     * @param end 结束点
     * @return 两点间距离，单位m
     */
    public static double getDistance(Coordinate start, Coordinate end)
    {
        final double v = Math.PI / Constants.ANGLE_HALF_CIRCLE;
        double lon1 = v * start.getLongitude();
        double lon2 = v * end.getLongitude();
        double lat1 = v * start.getLatitude();
        double lat2 = v * end.getLatitude();
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * Constants.ERATH_RADIUS;
        return d * Constants.ONE_KILOMETER;
    }

}
