package com.qhieco.webservice.impl;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.DiscountPackageRequest;
import com.qhieco.request.web.PackageOrderRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.DiscountPackageMapper;
import com.qhieco.webservice.DiscountPackageService;
import com.qhieco.webservice.exception.ExcelException;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:46
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class DiscountPackageServiceImpl implements DiscountPackageService {

    @Autowired
    DiscountPackageMapper discountPackageMapper;

    /**
     * 分页查询套餐列表
     * @param request
     * @return
     */
    @Override
    public Resp findDiscountPackageByPage(DiscountPackageRequest request) {
        List<DiscountPackageData> list = discountPackageMapper.pageDiscountPackage(request);
        Integer count = discountPackageMapper.pageDiscountPackageTotalCount(request);
        AbstractPaged<DiscountPackageData> data = AbstractPaged.<DiscountPackageData>builder().sEcho
                (request.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(list).build();
        return RespUtil.successResp(data);
    }

    /**
     * 分页统计套餐列表
     * @param request
     * @return
     */
    @Override
    public Resp findPackageStaticByPage(DiscountPackageRequest request) {
        List<DiscountPackageData> list = discountPackageMapper.pagePackageStatic(request);
        for (int i = 0; i < list.size(); i++) {
            DiscountPackageData item = list.get(i);
            int inNumber = item.getInNumber();
            if (inNumber > 0) {
                item.setTermState("进行中");
            }
            if (inNumber > -15 && inNumber <= 0) {
                item.setTermState("已到期");
            }
            if (inNumber < -15) {
                item.setTermState("已过期");
            }
        }
        Integer count = discountPackageMapper.pagePackageStaticTotalCount(request);
        AbstractPaged<DiscountPackageData> data = AbstractPaged.<DiscountPackageData>builder().sEcho
                (request.getSEcho() + 1).iTotalRecords(count).iTotalDisplayRecords(count).dataList(list).build();
        return RespUtil.successResp(data);
    }

    /**
     * 根据ID查询套餐详情
     *
     * @param request
     * @return
     */
    @Override
    public Resp findPackageDetailed(DiscountPackageRequest request) {
        DiscountPackageData item = discountPackageMapper.findPackageDetailed(request);
        List<DiscountRuleTimeData> ruleTimeList = discountPackageMapper.findRuleTimeList(request);
        item.setRuleTimeList(ruleTimeList);
        if (ruleTimeList != null && ruleTimeList.size() > 0) {
            item.setRuleType(ruleTimeList.get(0).getType());
        }
        List<DiscountFormatSumData> sumList = discountPackageMapper.findFormatSumList(request);
        item.setFormatSumList(sumList);

        return RespUtil.successResp(item);
    }

    /**
     * 修改套餐状态
     *
     * @param request
     * @return
     */
    @Override
    public Resp updatePackageState(DiscountPackageRequest request) {
        request.setUpdateTime(System.currentTimeMillis());
        discountPackageMapper.updateDiscountPackageData(request);
        return RespUtil.successResp();
    }

    /**
     * 修改套餐状态
     *
     * @param request
     * @return
     */
    @Override
    public Resp updateParklotState(DiscountPackageRequest request) {
        request.setUpdateTime(System.currentTimeMillis());
        discountPackageMapper.updateParklotState(request);
        return RespUtil.successResp();
    }

    /**
     * 操作套餐方法
     *
     * @param request
     * @return
     */
    public int savePackageDate(DiscountPackageRequest request) {
        Integer id = request.getId();
        int flat = 1;
        if (id == null) {
            request.setCreateTime(System.currentTimeMillis());
            flat = discountPackageMapper.insertDiscountPackageData(request);
        } else {
            request.setUpdateTime(System.currentTimeMillis());
            flat = discountPackageMapper.updateDiscountPackageData(request);

            if (flat > 0) {
                // 更新时间段数据，旧数据更改状态
                discountPackageMapper.updateRuleTimeState(request);
            }
        }
        // 更新套餐规格数据
        this.saveFormatSumByPackage(request.getPackageAmount(),request.getId());
        return flat;
    }

    /**
     * 新增/修改套餐详细
     * @param request
     * @return
     */
    @Override
    public Resp saveOrUpdate(DiscountPackageRequest request) {
        Integer ruleType = request.getRuleType();
        //每天
        if (ruleType == 1) {
            String[] everyDay = request.getEveryDay();
            if (verificationTime(everyDay)) {
                return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
            } else {
                int flat = savePackageDate(request);
                if (flat > 0) {
                    for (int i = 0; i < everyDay.length; i++) {
                        String itemTime = everyDay[i];
                        if (itemTime.length() > 5) {
                            String beginTime = itemTime.substring(0, itemTime.indexOf("-"));
                            String endTime = itemTime.substring(itemTime.indexOf("-") + 1, itemTime.length());
                            request.setRuleTimeBegin(beginTime);
                            request.setRuleTimeEnd(endTime);
                            request.setRuleType(1);
                            discountPackageMapper.insertRuleTimeData(request);
                            request.setRuleTimeBegin(null);
                            request.setRuleTimeEnd(null);
                        }
                    }
                }
            }
        } else {
            String[] workDay = request.getWorkDay();    //工作日
            String[] weekDay = request.getWeekDay();    //周末
            if (workDay == null || weekDay == null || workDay.length == 0 || weekDay.length == 0) {
                // 主动抛出异常，时间段有误
                return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
            }
            if (verificationTime(workDay) || verificationTime(weekDay)) {
                return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
            } else {
                int flat = savePackageDate(request);
                if (flat > 0) {
                    // 工作日
                    for (int i = 0; i < workDay.length; i++) {
                        String itemTime = workDay[i];
                        if (itemTime.length() > 5) {
                            String beginTime = itemTime.substring(0, itemTime.indexOf("-"));
                            String endTime = itemTime.substring(itemTime.indexOf("-") + 1, itemTime.length());
                            request.setRuleTimeBegin(beginTime);
                            request.setRuleTimeEnd(endTime);
                            request.setRuleType(2);
                            discountPackageMapper.insertRuleTimeData(request);
                            request.setRuleTimeBegin(null);
                            request.setRuleTimeEnd(null);
                        }
                    }

                    // 周末
                    for (int i = 0; i < weekDay.length; i++) {
                        String itemTime = weekDay[i];
                        if (itemTime.length() > 7) {
                            String beginTime = itemTime.substring(0, itemTime.indexOf("-"));
                            String endTime = itemTime.substring(itemTime.indexOf("-") + 1, itemTime.length());
                            request.setRuleTimeBegin(beginTime);
                            request.setRuleTimeEnd(endTime);
                            request.setRuleType(3);
                            discountPackageMapper.insertRuleTimeData(request);
                            request.setRuleTimeBegin(null);
                            request.setRuleTimeEnd(null);
                        }
                    }
                }
            }
        }
        return RespUtil.successResp();
    }

    public Resp saveFormatSumByPackage(String packageAmount, Integer packageId) {
        try {
            discountPackageMapper.delFormatSumData(packageId);
            String daytime = packageAmount.substring(0, packageAmount.indexOf("-"));
            String sumNumber = packageAmount.substring(packageAmount.indexOf("-") + 1, packageAmount.length());
            String[] str1 = daytime.split(",");
            String[] str2 = sumNumber.split(",");
            if (str1.length == str2.length) {
                for (int i = 0; i < str1.length; i++) {
                    discountPackageMapper.insertFormatSumData(str1[i], str2[i], packageId);
                }
                return RespUtil.successResp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
    }

    /**
     * String To Date
     * @param strDate
     * @return
     */
    public Date strToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sdf.parse(strDate, pos);
        return strtodate;
    }

    public boolean verificationTime(String[] time) {
        String date = "2018-05-01";
        try {
            List<Map<String, Long>> list = new ArrayList<>();
            // 判断时间是否存在跨天并初始化
            for (int i = 0; i < time.length; i++) {
                String itemTime = time[i];
                int index = itemTime.lastIndexOf("-");
                if (index == -1) {
                    return true;
                }
                index = itemTime.lastIndexOf(":");
                if (index == -1) {
                    return true;
                }

                String beginTime = date + " " + itemTime.substring(0, itemTime.indexOf("-"));
                String endTime = date + " " + itemTime.substring(itemTime.indexOf("-") + 1, itemTime.length());
                int result = beginTime.compareTo(endTime);
                Map<String, Long> map = new HashMap<>();
                if (result >= 0) {
                    // 跨天，天数+1
                    Long begin = strToDate(beginTime).getTime();

                    Date item = strToDate(endTime);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(item);
                    calendar.add(Calendar.DATE, 1);

                    Long end = calendar.getTime().getTime();
                    map.put("begin", begin);
                    map.put("end", end);
                    list.add(map);
                } else {
                    Long begin = strToDate(beginTime).getTime();
                    Long end = strToDate(endTime).getTime();
                    map.put("begin", begin);
                    map.put("end", end);
                    list.add(map);
                }
            }

            // 计算时间是否重复
            for (int i = 0; i < list.size(); i++) {
                Map<String, Long> map1 = list.get(i);
                Long begin1 = map1.get("begin");
                Long end1 = map1.get("end");
                for (int j = 0; j < list.size(); j++) {
                    if (j != i) {
                        Map<String, Long> map2 = list.get(j);
                        Long begin2 = map2.get("begin");
                        Long end2 = map2.get("end");

                        if ((begin1 <= begin2 && end1 > begin2) || (begin1 >= begin2 && begin1 < end2)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    /**
     * 导出套餐列表
     * @param request
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    @Override
    public Resp excelPackage(DiscountPackageRequest request, OutputStream outputStream, Class cl) throws IOException {
        request.setLength(5000);
        request.setStart(0);
        List<DiscountPackageData> userDataList = discountPackageMapper.excelPackage(request);
        if (userDataList != null && userDataList.size() == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(DiscountPackageData items:userDataList){
            items.setStateStr(Status.PackageState.find(items.getState()));
        }

        ExcelUtil<DiscountPackageData> packageExcelUtil = new ExcelUtil<>(outputStream, DiscountPackageData.class);
        packageExcelUtil.write(userDataList);
        return RespUtil.successResp();
    }

    /**
     * 导出套餐统计列表
     * @param request
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    @Override
    public Resp excelStaticPackage(DiscountPackageRequest request, OutputStream outputStream, Class cl) throws IOException {
        request.setLength(5000);
        request.setStart(0);
        List<DiscountPackageStaticData> userDataList = discountPackageMapper.excelStaticPackage(request);
        for (int i = 0; i < userDataList.size(); i++) {
            DiscountPackageStaticData item = userDataList.get(i);
            int inNumber = item.getInNumber();
            if (inNumber > 0) {
                item.setTermState("进行中");
            }
            if (inNumber > -15 && inNumber <= 0) {
                item.setTermState("已到期");
            }
            if (inNumber < -15) {
                item.setTermState("已过期");
            }
        }
        if (userDataList != null && userDataList.size() == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }

        ExcelUtil<DiscountPackageStaticData> packageExcelUtil = new ExcelUtil<>(outputStream, DiscountPackageStaticData.class);
        packageExcelUtil.write(userDataList);
        return RespUtil.successResp();
    }

    public static void main(String[] args) {
//        saveFormatSumByPackages("12,30,50-1,2,3",1);
//        DiscountPackageRequest request = new DiscountPackageRequest();
//        request.setEveryDay(new String[]{"10:00:00-11:01:00","12:00:00-13:00:00"});
//        request.setRuleType(1);
//        saveOrUpdates(request);
    }
}
