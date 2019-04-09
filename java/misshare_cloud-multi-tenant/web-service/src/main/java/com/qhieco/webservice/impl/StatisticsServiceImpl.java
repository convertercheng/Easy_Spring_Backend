package com.qhieco.webservice.impl;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.StatisticsRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.StatisticsData;
import com.qhieco.util.DateUtils;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.StatisticsMapper;
import com.qhieco.webservice.StatisticsService;
import com.qhieco.webservice.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/13 19:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    StatisticsMapper statisticsMapper;

    /**
     * 根据条件获取统计数据
     * @param request
     */
    @Override
    public Resp findStatisticsByActivityType(StatisticsRequest request) {
        List<StatisticsData> resultList = new ArrayList<>();
        String type = request.getDateType();
        long second = ((request.getEndStatistTime() - request.getBeginStatistTime()) / 1000);
        if (second <= (24 * 60 * 60)) {
            type = "日";
        }
        List<StatisticsData> itemList = new ArrayList<>();
        switch (type) {
            case "日":
                request.setDateType("hours");
                itemList = statisticsMapper.findStatisticsByActivityType(request);
                resultList = processStatisticsByDay(itemList, request);
                break;
            case "周":
                request.setDateType("days");
                itemList = statisticsMapper.findStatisticsByActivityType(request);
                resultList = processStatisticsByWeek(itemList, request);
                break;
            case "月":
                request.setDateType("days");
                itemList = statisticsMapper.findStatisticsByActivityType(request);
                resultList = processStatisticsByMonth(itemList, request);
                break;
            default:
                request.setDateType("days");
                itemList = statisticsMapper.findStatisticsByActivityType(request);
                resultList = processStatisticsByDefaultDate(itemList, request);
        }
        return RespUtil.successResp(resultList);
    }

    @Override
    public Resp getActivityOffCountData(StatisticsRequest request) {
        List<StatisticsData> resultList = new ArrayList<>();
        String type = request.getDateType();
        long second = ((request.getEndStatistTime() - request.getBeginStatistTime()) / 1000);
        if (second <= (24 * 60 * 60 - 1)) {
            type = "日";
        }
        List<StatisticsData> itemList = new ArrayList<>();
        switch (type) {
            case "日":
                request.setDateType("hours");
                itemList = statisticsMapper.getActivityOffCountData(request);
                resultList = processStatisticsByDay(itemList, request);
                break;
            case "周":
                //request.setDateType("weeks");
                request.setDateType("days");
                itemList = statisticsMapper.getActivityOffCountData(request);
                resultList = processStatisticsByWeek(itemList, request);
                break;
            case "月":
                request.setDateType("days");
                itemList = statisticsMapper.getActivityOffCountData(request);
                resultList = processStatisticsByMonth(itemList, request);
                break;
            default:
                request.setDateType("days");
                itemList = statisticsMapper.getActivityOffCountData(request);
                resultList = processStatisticsByDefaultDate(itemList, request);
        }
        return RespUtil.successResp(resultList);
    }

    @Override
    public Resp getActivityCountByList(StatisticsRequest request) {
        List<StatisticsData> resultList = statisticsMapper.getActivityCountByList(request);
        for (StatisticsData statisticsData : resultList) {
            statisticsData.getLaTypeStr();
            if (statisticsData.getLaType() == 1) {
                // 被邀请类型
                statisticsData.setTargetPhone(statisticsData.getTarget1Phone());
            } else if (statisticsData.getLaType() == 2) {
                // 邀请类型
                statisticsData.setTargetPhone(statisticsData.getTarget2Phone());
            }
//            String triggerTypesStr = "";
//            String item = statisticsData.getTriggerTypes();
//            if (item!=null&&item.indexOf(",") != -1) {
//                String[] types = item.split(",");
//                for (int i = 0; i < types.length; i++) {
//                    if (i == 0) {
//                        triggerTypesStr = types[i];
//                    } else {
//                        triggerTypesStr = triggerTypesStr+"," + types[i];
//                    }
//                }
//            }
//            statisticsData.setTriggerTypesStr(triggerTypesStr);
        }
        Integer count = statisticsMapper.pageActivityCountTotalCount(request);
        AbstractPaged<StatisticsData> datas = AbstractPaged.<StatisticsData>builder().sEcho(request.getSEcho() + 1).iTotalRecords(count).iTotalDisplayRecords(count).dataList(resultList).build();
        return RespUtil.successResp(datas);
    }

    @Override
    public Resp excel(StatisticsRequest request, OutputStream outputStream, Class cl) throws IOException {
        request.setLength(5000);
        request.setStart(0);
        List<StatisticsData> statisticsDataList = statisticsMapper.getActivityCountByList(request);

        if (statisticsDataList != null && statisticsDataList.size() == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        // 根据活动类型，匹配对应的excelFileName
        String fileName = null;
        if (request.getActivityType() == 1) {
            fileName = "StatisticsData1";
        } else {
            fileName = "StatisticsData2";
        }

        ExcelUtil<StatisticsData> statisticsDataExcelUtil = new ExcelUtil<>(outputStream, StatisticsData.class, fileName);
        if (statisticsDataList.size() > 0) {
            for (StatisticsData statisticsData : statisticsDataList) {
                statisticsData.getLaTypeStr();
                if (statisticsData.getLaType() == 1) {
                    // 被邀请类型
                    statisticsData.setTargetPhone(statisticsData.getTarget1Phone());
                } else if (statisticsData.getLaType() == 2) {
                    // 邀请类型
                    statisticsData.setTargetPhone(statisticsData.getTarget2Phone());
                }

                String str = "";
                if (!StringUtils.isEmpty(statisticsData.getTriggerTypes())) {
                    String[] arr = statisticsData.getTriggerTypes().split(Constants.DELIMITER_COMMA);
                    for (String s : arr) {
                        try {
                            str += Status.TriggerType.find(Integer.valueOf(s)) + ";";
                        } catch (Exception e) {
                            log.error("" + e);
                        }
                    }
                }
                statisticsData.setTriggerTypesStr(str);
            }
            ExcelUtil<StatisticsData> excelUtil = new ExcelUtil<>(outputStream, StatisticsData.class);
            excelUtil.write(statisticsDataList);
        }
        return RespUtil.successResp();
    }

    /**
     * 根据日期 Day 加工数据
     * @param statisList, request
     * @return
     */
    protected List<StatisticsData> processStatisticsByDay(List<StatisticsData> statisList, StatisticsRequest request) {
        List<StatisticsData> itemList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            boolean isExist = false;
            for (int j = 0; j < statisList.size(); j++) {
                int hours = statisList.get(j).getHours();
                if (hours == i) {
                    itemList.add(statisList.get(j));
                    isExist = true;
                }
            }
            if (!isExist) {
                itemList.add(new StatisticsData(i, null, null, null));
            }
        }
        statisList = null;
        return itemList;
    }

    /**
     * 根据日期 Week 加工数据
     * @param request
     * @return
     */
    protected List<StatisticsData> processStatisticsByWeek(List<StatisticsData> statisList, StatisticsRequest request) {
        List<StatisticsData> itemList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i <= 7; i++) {
            boolean isExist = false;
            for (int j = 0; j < statisList.size(); j++) {
                int weeks = statisList.get(j).getWeeks();
                if (weeks == i) {
                    itemList.add(statisList.get(j));
                    isExist = true;
                }
            }
            if (!isExist) {
                itemList.add(new StatisticsData(null, null, i, format.format(c.getTime())));
            }
            c.add(Calendar.DATE, -1);
        }

        // 排序
        Collections.sort(itemList, new Comparator<StatisticsData>() {
            @Override
            public int compare(StatisticsData o1, StatisticsData o2) {
                // 按照天数排序
                if (o1.getWeeks() > o2.getWeeks()) {
                    return 1;
                }
                if (o1.getWeeks() == o2.getWeeks()) {
                    return 0;
                }
                return -1;
            }
        });
        statisList = null;
        return itemList;
    }

    /**
     * 根据日期 Month 加工数据
     * @param request
     * @return
     */
    protected List<StatisticsData> processStatisticsByMonth(List<StatisticsData> statisList, StatisticsRequest request) {
        List<StatisticsData> itemList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);

        for (int i = 0; i <= 30; i++) {
            int day = c.get(Calendar.DATE);
            boolean isExist = false;
            for (int j = 0; j < statisList.size(); j++) {
                int days = statisList.get(j).getDays();
                if (days == day) {
                    itemList.add(statisList.get(j));
                    isExist = true;
                }
            }
            if (!isExist) {
                itemList.add(new StatisticsData(null, c.get(Calendar.DATE), null, format.format(c.getTime())));
            }
            c.add(Calendar.DATE, -1);
        }

        // 排序
        strSort(itemList);
        statisList = null;
        return itemList;
    }

    /**
     * 默认日期 加工数据
     * @param request
     * @return
     */
    protected List<StatisticsData> processStatisticsByDefaultDate(List<StatisticsData> statisList, StatisticsRequest request) {
        List<StatisticsData> itemList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        long bightDate = request.getBeginStatistTime();
        long endDate = request.getEndStatistTime();
        Date today = new Date(endDate);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int difference = DateUtils.differentDays(bightDate, endDate);
        // ！默认天数不能超过5000
        if (difference > 5000) {
            difference = 5000;
        }
        for (int i = 0; i <= difference; i++) {
            int day = c.get(Calendar.DATE);
            boolean isExist = false;
            for (int j = 0; j < statisList.size(); j++) {
                int days = statisList.get(j).getDays();
                if (days == day) {
                    itemList.add(statisList.get(j));
                    isExist = true;
                }
            }
            if (!isExist) {
                itemList.add(new StatisticsData(null, c.get(Calendar.DATE), null, format.format(c.getTime())));
            }
            c.add(Calendar.DATE, -1);
        }

        // 排序
        strSort(itemList);

        statisList = null;
        return itemList;
    }

    public static void strSort(List<StatisticsData> itemList){
        for (int i = 0; i < itemList.size(); i++) {
            for (int j = i+1; j < itemList.size(); j++) {
                if(itemList.get(i).getDaysDate().compareTo(itemList.get(j).getDaysDate())>0){    //对象排序用camparTo方法
                    swap(itemList,i,j);
                }
            }
        }

    }

    /**
     * 交换两个元素的位置的方法
     * @param i    索引i
     * @param j 索引j
     */
    private static void swap(List<StatisticsData> itemList, int i, int j) {
        StatisticsData t = itemList.get(i);
        itemList.set(i,itemList.get(j));
        itemList.set(j,t);
    }
}
