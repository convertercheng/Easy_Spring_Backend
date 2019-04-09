package com.qhieco.webservice.impl;

import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonrepo.CouponRepository;
import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.commonrepo.OrderRefundRepository;
import com.qhieco.commonrepo.OrderWithdrawRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.FinanceRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TenantContext;
import com.qhieco.util.TimeUtil;
import com.qhieco.webservice.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午8:04
 * <p>
 * 类说明：
 *     财务
 */
@Service
@Slf4j
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    OrderRefundRepository orderRefundRepository;

    @Autowired
    OrderWithdrawRepository orderWithdrawRepository;

    @Autowired
    CouponRepository couponRepository;

    @Override
    @EnableTenantFilter
    public Resp total(FinanceRequest financeRequest){
        try {
            Long startTime = financeRequest.getStartTime();
            Long endTime = financeRequest.getEndTime();
            Integer type = financeRequest.getType();
            List<FinanceData.Finance> financeList = new ArrayList<>();
            if (startTime == null || endTime == null) {
                updateFinanceList(financeList);
            }
            else {
                Calendar calendar = Calendar.getInstance();
                if (type.equals(Constants.DAY)) {
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getDay2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getDay2Mseconds() * (i + 1);
                        updateFinanceList(retStartTime, retEndTime, financeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.WEEK)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取下一个最近的周日
                    while (dayOfWeek != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取上一个最近的周六
                    while (dayOfWeek != 7) {
                        endTime -= TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(endTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getWeek2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getWeek2Mseconds() * (i + 1);
                        updateFinanceList(retStartTime, retEndTime, financeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.MONTH)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // 获取下一个最近的1号
                    while (dayOfMonth != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month1 = calendar.get(Calendar.MONTH);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    int month2 = calendar.get(Calendar.MONTH);
                    // 获取上一个最近的月末一天
                    if (month1 == month2) {
                        endTime -= TimeUtil.getDay2Mseconds() * dayOfMonth;
                    }
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("retEndTime: {}, endTime: {}", retEndTime, endTime);
                        calendar.setTimeInMillis(retEndTime);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int numOfDays = TimeUtil.days(year, month);
                        retStartTime = retEndTime;
                        retEndTime = retStartTime + TimeUtil.getDay2Mseconds() * numOfDays;
                        log.info("retStartTime: {}, retEndTime: {}", retStartTime, retEndTime);
                        updateFinanceList(retStartTime, retEndTime, financeList);
                    }
                }
            }
            FinanceData financeData = new FinanceData(financeList);
            return RespUtil.successResp(financeData);
        } catch (Exception e) {
            log.error("get finance failed.");
            e.printStackTrace();
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
    }


    public void updateFinanceList(Long startTime, Long endTime, List<FinanceData.Finance> financeList) {
        BigDecimal orderFee = orderParkingRepository.sumOfRealFeeByPayTime(startTime,endTime);
        BigDecimal refundFee = orderRefundRepository.sumOfFeeByUpdateTimeAndState(startTime, endTime,Status.Refund.PROCESS_SUCCESS_TOTAL.getInt());
        BigDecimal withdrawFee = orderWithdrawRepository.sumOfBalanceByCompleteTimeAndState(startTime, endTime,Status.Withdraw.PROCESS_SUCCESS.getInt());
        orderFee = (orderFee == null) ? Constants.BIGDECIMAL_ZERO : orderFee;
        withdrawFee = (withdrawFee == null) ? Constants.BIGDECIMAL_ZERO : withdrawFee;
        refundFee = (refundFee == null) ? Constants.BIGDECIMAL_ZERO : refundFee;
        if(TenantContext.getCurrentTenant() != null) {
            withdrawFee = Constants.BIGDECIMAL_ZERO;
        }
        BigDecimal inflow = orderFee;
        BigDecimal outflow = withdrawFee.add(refundFee);
        BigDecimal balance = inflow.subtract(outflow);
        FinanceData financeData = new FinanceData();
        FinanceData.Finance finance = financeData.new Finance(inflow.toString(), outflow.toString(), balance.toString(), startTime);
        financeList.add(finance);
    }


    public void updateFinanceList(List<FinanceData.Finance> financeList) {
        BigDecimal orderFee = orderParkingRepository.sumOfRealFee();
        BigDecimal refundFee = orderRefundRepository.sumOfFeeByState(Status.Refund.PROCESS_SUCCESS_TOTAL.getInt());
        BigDecimal withdrawFee = orderWithdrawRepository.sumOfBalanceByState(Status.Withdraw.PROCESS_SUCCESS.getInt());
        orderFee = (orderFee == null) ? Constants.BIGDECIMAL_ZERO : orderFee;
        withdrawFee = (withdrawFee == null) ? Constants.BIGDECIMAL_ZERO : withdrawFee;
        refundFee = (refundFee == null) ? Constants.BIGDECIMAL_ZERO : refundFee;
        if(TenantContext.getCurrentTenant() != null) {
            withdrawFee = Constants.BIGDECIMAL_ZERO;
        }
        BigDecimal inflow = orderFee;
        BigDecimal outflow = withdrawFee.add(refundFee);
        BigDecimal balance = inflow.subtract(outflow);
        FinanceData financeData = new FinanceData();
        FinanceData.Finance finance = financeData.new Finance(inflow.toString(), outflow.toString(), balance.toString(),null);
        financeList.add(finance);
    }


    @Override
    public Resp tripartiteFee(FinanceRequest financeRequest){
        try {
            Long startTime = financeRequest.getStartTime();
            Long endTime = financeRequest.getEndTime();
            Integer type = financeRequest.getType();
            List<TripartiteFeeData.TripartiteFee> tripartiteFeeList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            if (startTime == null || endTime == null) {
                updateTripartiteFeeList(tripartiteFeeList);
            }
            else {
                if (type.equals(Constants.DAY)) {
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("i: {}, retEndTime: {}, endTime: {}", i, retEndTime, endTime);
                        retStartTime = startTime + TimeUtil.getDay2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getDay2Mseconds() * (i + 1);
                        updateTripartiteFeeList(retStartTime, retEndTime, tripartiteFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.WEEK)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取下一个最近的周日
                    while (dayOfWeek != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取上一个最近的周六
                    while (dayOfWeek != 7) {
                        endTime -= TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(endTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getWeek2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getWeek2Mseconds() * (i + 1);
                        updateTripartiteFeeList(retStartTime, retEndTime, tripartiteFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.MONTH)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // 获取下一个最近的1号
                    while (dayOfMonth != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    log.info("startTime: {}, dayOfMonth: {}", startTime, dayOfMonth);
                    calendar.setTimeInMillis(endTime);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month1 = calendar.get(Calendar.MONTH);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    int month2 = calendar.get(Calendar.MONTH);
                    // 获取上一个最近的月末一天
                    if (month1 == month2) {
                        endTime -= TimeUtil.getDay2Mseconds() * dayOfMonth;
                    }
                    log.info("endTime: {}, dayOfMonth: {}", endTime, dayOfMonth);
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("retEndTime: {}, endTime: {}", retEndTime, endTime);
                        calendar.setTimeInMillis(retEndTime);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int numOfDays = TimeUtil.days(year, month);
                        retStartTime = retEndTime;
                        retEndTime = retStartTime + TimeUtil.getDay2Mseconds() * numOfDays;
                        log.info("retStartTime: {}, retEndTime: {}", retStartTime, retEndTime);
                        updateTripartiteFeeList(retStartTime, retEndTime, tripartiteFeeList);
                    }
                }
            }
            TripartiteFeeData tripartiteFeeData = new TripartiteFeeData(tripartiteFeeList);
            return RespUtil.successResp(tripartiteFeeData);
        } catch (Exception e) {
            log.error("get tripartiteFee failed.");
            e.printStackTrace();
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
    }


    public void updateTripartiteFeeList(Long startTime, Long endTime, List<TripartiteFeeData.TripartiteFee> tripartiteFeeList) {
        BigDecimal zfbFee = orderParkingRepository.sumOfRealFeeByPayTimeAndPayChannel(startTime, endTime, Constants.PAY_CHANNEL_ALIPAY);
        BigDecimal wxFee = orderParkingRepository.sumOfRealFeeByPayTimeAndPayChannel(startTime, endTime, Constants.PAY_CHANNEL_WXPAY);
        zfbFee = (zfbFee == null) ? Constants.BIGDECIMAL_ZERO : zfbFee;
        wxFee = (wxFee == null) ? Constants.BIGDECIMAL_ZERO : wxFee;
        BigDecimal sum = zfbFee.add(wxFee);
        TripartiteFeeData tripartiteFeeData = new TripartiteFeeData();
        TripartiteFeeData.TripartiteFee tripartiteFee = tripartiteFeeData.new TripartiteFee(sum.toString(), zfbFee.toString(), wxFee.toString(), startTime);
        tripartiteFeeList.add(tripartiteFee);
    }

    public void updateTripartiteFeeList(List<TripartiteFeeData.TripartiteFee> tripartiteFeeList) {
        BigDecimal zfbFee = orderParkingRepository.sumOfRealFeeByPayChannel(Constants.PAY_CHANNEL_ALIPAY);
        BigDecimal wxFee = orderParkingRepository.sumOfRealFeeByPayChannel(Constants.PAY_CHANNEL_WXPAY);
        zfbFee = (zfbFee == null) ? Constants.BIGDECIMAL_ZERO : zfbFee;
        wxFee = (wxFee == null) ? Constants.BIGDECIMAL_ZERO : wxFee;
        BigDecimal sum = zfbFee.add(wxFee);
        TripartiteFeeData tripartiteFeeData = new TripartiteFeeData();
        TripartiteFeeData.TripartiteFee tripartiteFee = tripartiteFeeData.new TripartiteFee(sum.toString(), zfbFee.toString(), wxFee.toString(), null);
        tripartiteFeeList.add(tripartiteFee);
    }


    @Override
    public Resp invoiceFee(FinanceRequest financeRequest){
        try {
            Long startTime = financeRequest.getStartTime();
            Long endTime = financeRequest.getEndTime();
            Integer type = financeRequest.getType();
            List<InvoiceFeeData.InvoiceFee> invoiceFeeList = new ArrayList<>();
            if (startTime == null || endTime == null) {
                updateInvoiceFeeList(invoiceFeeList);
            }
            else {
                Calendar calendar = Calendar.getInstance();
                if (type.equals(Constants.DAY)) {
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getDay2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getDay2Mseconds() * (i + 1);
                        updateInvoiceFeeList(retStartTime, retEndTime, invoiceFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.WEEK)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取下一个最近的周日
                    while (dayOfWeek != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取上一个最近的周六
                    while (dayOfWeek != 7) {
                        endTime -= TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(endTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getWeek2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getWeek2Mseconds() * (i + 1);
                        updateInvoiceFeeList(retStartTime, retEndTime, invoiceFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.MONTH)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // 获取下一个最近的1号
                    while (dayOfMonth != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month1 = calendar.get(Calendar.MONTH);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    int month2 = calendar.get(Calendar.MONTH);
                    // 获取上一个最近的月末一天
                    if (month1 == month2) {
                        endTime -= TimeUtil.getDay2Mseconds() * dayOfMonth;
                    }
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("retEndTime: {}, endTime: {}", retEndTime, endTime);
                        calendar.setTimeInMillis(retEndTime);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int numOfDays = TimeUtil.days(year, month);
                        retStartTime = retEndTime;
                        retEndTime = retStartTime + TimeUtil.getDay2Mseconds() * numOfDays;
                        log.info("retStartTime: {}, retEndTime: {}", retStartTime, retEndTime);
                        updateInvoiceFeeList(retStartTime, retEndTime, invoiceFeeList);
                    }
                }
            }
            InvoiceFeeData invoiceFeeData = new InvoiceFeeData(invoiceFeeList);
            return RespUtil.successResp(invoiceFeeData);
        } catch (Exception e) {
            log.error("get invoiceFee failed.");
            e.printStackTrace();
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
    }

    public void updateInvoiceFeeList(Long startTime, Long endTime, List<InvoiceFeeData.InvoiceFee> invoiceFeeList) {
        BigDecimal invoiceFeeTotal = orderParkingRepository.sumOfRealFeeByPayTimeAndInvoiceStateIsNotNull(startTime,endTime);
        BigDecimal invoiceFeeExecuted = orderParkingRepository.sumOfRealFeeByPayTimeAndInvoiceState(startTime, endTime, Status.InvoiceStatus.MAKED_SUCCESS.getInt());
        invoiceFeeTotal = (invoiceFeeTotal == null) ? Constants.BIGDECIMAL_ZERO : invoiceFeeTotal;
        invoiceFeeExecuted = (invoiceFeeExecuted == null) ? Constants.BIGDECIMAL_ZERO : invoiceFeeExecuted;
        BigDecimal invoiceFeeUnexecuted = invoiceFeeTotal.subtract(invoiceFeeExecuted);
        InvoiceFeeData invoiceFeeData = new InvoiceFeeData();
        InvoiceFeeData.InvoiceFee invoiceFee = invoiceFeeData.new InvoiceFee(invoiceFeeTotal.toString(), invoiceFeeExecuted.toString(), invoiceFeeUnexecuted.toString(), startTime);
        invoiceFeeList.add(invoiceFee);
    }

    public void updateInvoiceFeeList(List<InvoiceFeeData.InvoiceFee> invoiceFeeList) {
        BigDecimal invoiceFeeTotal = orderParkingRepository.sumOfRealFeeByInvoiceStateIsNotNull();
        BigDecimal invoiceFeeExecuted = orderParkingRepository.sumOfRealFeeByInvoiceState(Status.InvoiceStatus.MAKED_SUCCESS.getInt());
        invoiceFeeTotal = (invoiceFeeTotal == null) ? Constants.BIGDECIMAL_ZERO : invoiceFeeTotal;
        invoiceFeeExecuted = (invoiceFeeExecuted == null) ? Constants.BIGDECIMAL_ZERO : invoiceFeeExecuted;
        BigDecimal invoiceFeeUnexecuted = invoiceFeeTotal.subtract(invoiceFeeExecuted);
        InvoiceFeeData invoiceFeeData = new InvoiceFeeData();
        InvoiceFeeData.InvoiceFee invoiceFee = invoiceFeeData.new InvoiceFee(invoiceFeeTotal.toString(), invoiceFeeExecuted.toString(), invoiceFeeUnexecuted.toString(), null);
        invoiceFeeList.add(invoiceFee);
    }


    @Override
    public Resp withdrawFee(FinanceRequest financeRequest){
            Long startTime = financeRequest.getStartTime();
            Long endTime = financeRequest.getEndTime();
            Integer type = financeRequest.getType();
            List<WithdrawFeeData.WithdrawFee> withdrawFeeList = new ArrayList<>();
            if (startTime == null || endTime == null) {
                updateWithdrawFeeList(withdrawFeeList);
            }
            else {
                Calendar calendar = Calendar.getInstance();
                if (type.equals(Constants.DAY)) {
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getDay2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getDay2Mseconds() * (i + 1);
                        updateWithdrawFeeList(retStartTime, retEndTime, withdrawFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.WEEK)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取下一个最近的周日
                    while (dayOfWeek != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取上一个最近的周六
                    while (dayOfWeek != 7) {
                        endTime -= TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(endTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getWeek2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getWeek2Mseconds() * (i + 1);
                        updateWithdrawFeeList(retStartTime, retEndTime, withdrawFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.MONTH)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // 获取下一个最近的1号
                    while (dayOfMonth != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month1 = calendar.get(Calendar.MONTH);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    int month2 = calendar.get(Calendar.MONTH);
                    // 获取上一个最近的月末一天
                    if (month1 == month2) {
                        endTime -= TimeUtil.getDay2Mseconds() * dayOfMonth;
                    }
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("retEndTime: {}, endTime: {}", retEndTime, endTime);
                        calendar.setTimeInMillis(retEndTime);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int numOfDays = TimeUtil.days(year, month);
                        retStartTime = retEndTime;
                        retEndTime = retStartTime + TimeUtil.getDay2Mseconds() * numOfDays;
                        log.info("retStartTime: {}, retEndTime: {}", retStartTime, retEndTime);
                        updateWithdrawFeeList(retStartTime, retEndTime, withdrawFeeList);
                    }
                }
            }
            WithdrawFeeData withdrawFeeData = new WithdrawFeeData(withdrawFeeList);
            return RespUtil.successResp(withdrawFeeData);
        }

    public void updateWithdrawFeeList(Long startTime, Long endTime, List<WithdrawFeeData.WithdrawFee> withdrawFeeList) {
        List<Object> feeAndTypeList = orderWithdrawRepository.getBalanceAndType(startTime, endTime, Status.Withdraw.PROCESS_SUCCESS.getInt());
        BigDecimal userFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_RENTEE*1);
        BigDecimal ownerFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_RENTER*1);
        BigDecimal estateFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_ADMIN*1);
        WithdrawFeeData withdrawFeeData = new WithdrawFeeData();
        WithdrawFeeData.WithdrawFee withdrawFee = withdrawFeeData.new WithdrawFee(userFee.toString(), ownerFee.toString(), estateFee.toString(), startTime);
        withdrawFeeList.add(withdrawFee);
    }

    public void updateWithdrawFeeList(List<WithdrawFeeData.WithdrawFee> withdrawFeeList) {
        List<Object> feeAndTypeList = orderWithdrawRepository.getBalanceAndType(Status.Withdraw.PROCESS_SUCCESS.getInt());
        BigDecimal userFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_RENTEE*1);
        BigDecimal ownerFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_RENTER*1);
        BigDecimal estateFee = sumWithdrawFee(feeAndTypeList, Constants.PARKING_ADMIN*1);
        WithdrawFeeData withdrawFeeData = new WithdrawFeeData();
        WithdrawFeeData.WithdrawFee withdrawFee = withdrawFeeData.new WithdrawFee(userFee.toString(), ownerFee.toString(), estateFee.toString(), null);
        withdrawFeeList.add(withdrawFee);
    }

    public BigDecimal sumWithdrawFee(List<Object> feeAndTypeList, Integer userType) {
        BigDecimal withdrawFee = Constants.BIGDECIMAL_ZERO;
        for (Object feeAndType: feeAndTypeList) {
            Object [] realList = (Object []) feeAndType;
            BigDecimal fee = (BigDecimal) realList[0];
            Byte  type = (Byte) realList[1];
            if (type.intValue() != userType) {
                continue;
            }
            withdrawFee = withdrawFee.add(fee);
        }
        return withdrawFee;
    }


    @Override
    public Resp couponFee(FinanceRequest financeRequest){

        try {
            Long startTime = financeRequest.getStartTime();
            Long endTime = financeRequest.getEndTime();
            Integer type = financeRequest.getType();
            List<CouponFeeData.CouponFee> couponFeeList = new ArrayList<>();
            if (startTime == null || endTime == null) {
                updateCouponFeeList(couponFeeList);
            }
            else {
                Calendar calendar = Calendar.getInstance();
                if (type.equals(Constants.DAY)) {
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getDay2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getDay2Mseconds() * (i + 1);
                        updateCouponFeeList(retStartTime, retEndTime, couponFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.WEEK)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取下一个最近的周日
                    while (dayOfWeek != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // 获取上一个最近的周六
                    while (dayOfWeek != 7) {
                        endTime -= TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(endTime);
                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    }
                    int i = 0;
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        retStartTime = startTime + TimeUtil.getWeek2Mseconds() * i;
                        retEndTime = startTime + TimeUtil.getWeek2Mseconds() * (i + 1);
                        updateCouponFeeList(retStartTime, retEndTime, couponFeeList);
                        i += 1;
                    }
                } else if (type.equals(Constants.MONTH)) {
                    calendar.setTimeInMillis(startTime);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // 获取下一个最近的1号
                    while (dayOfMonth != 1) {
                        startTime += TimeUtil.getDay2Mseconds();
                        calendar.setTimeInMillis(startTime);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    calendar.setTimeInMillis(endTime);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month1 = calendar.get(Calendar.MONTH);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    int month2 = calendar.get(Calendar.MONTH);
                    // 获取上一个最近的月末一天
                    if (month1 == month2) {
                        endTime -= TimeUtil.getDay2Mseconds() * dayOfMonth;
                    }
                    Long retStartTime, retEndTime = startTime;
                    while (retEndTime < endTime) {
                        log.info("retEndTime: {}, endTime: {}", retEndTime, endTime);
                        calendar.setTimeInMillis(retEndTime);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int numOfDays = TimeUtil.days(year, month);
                        retStartTime = retEndTime;
                        retEndTime = retStartTime + TimeUtil.getDay2Mseconds() * numOfDays;
                        log.info("retStartTime: {}, retEndTime: {}", retStartTime, retEndTime);
                        updateCouponFeeList(retStartTime, retEndTime, couponFeeList);
                    }
                }
            }
            CouponFeeData couponFeeData = new CouponFeeData(couponFeeList);
            return RespUtil.successResp(couponFeeData);
        } catch (Exception e) {
            log.error("get couponFee failed.");
            e.printStackTrace();
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
    }

    public void updateCouponFeeList(Long startTime, Long endTime, List<CouponFeeData.CouponFee> couponFeeList) {
        BigDecimal couponUsedFee = couponRepository.findSumUsedMoneyByUserTimeAndState(startTime, endTime, Status.Coupon.USED.getInt());
        BigDecimal couponLimitFee = couponRepository.findSumLimitByCreateTime(startTime, endTime);
        couponUsedFee = (couponUsedFee == null) ? Constants.BIGDECIMAL_ZERO : couponUsedFee;
        couponLimitFee = (couponLimitFee == null) ? Constants.BIGDECIMAL_ZERO : couponLimitFee;
        CouponFeeData couponFeeData = new CouponFeeData();
        CouponFeeData.CouponFee couponFee = couponFeeData.new CouponFee(couponUsedFee.toString(),couponLimitFee.toString(), startTime);
        couponFeeList.add(couponFee);
    }

    public void updateCouponFeeList(List<CouponFeeData.CouponFee> couponFeeList) {
        BigDecimal couponUsedFee = couponRepository.findSumUsedMoneyByState(Status.Coupon.USED.getInt());
        BigDecimal couponLimitFee = couponRepository.findSumLimit();
        couponUsedFee = (couponUsedFee == null) ? Constants.BIGDECIMAL_ZERO : couponUsedFee;
        couponLimitFee = (couponLimitFee == null) ? Constants.BIGDECIMAL_ZERO : couponLimitFee;
        CouponFeeData couponFeeData = new CouponFeeData();
        CouponFeeData.CouponFee couponFee = couponFeeData.new CouponFee(couponUsedFee.toString(), couponLimitFee.toString(),null);
        couponFeeList.add(couponFee);
    }

}
