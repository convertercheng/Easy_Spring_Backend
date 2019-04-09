package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ApplyInvoiceRequest;
import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.request.web.OrderWithdrawRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.*;
import com.qhieco.webmapper.ApplyInvoiceMapper;
import com.qhieco.webmapper.OrderRefundMapper;
import com.qhieco.webmapper.OrderWithdrawMapper;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.OrderService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/5 下午5:20
 *          <p>
 *          类说明：
 *          OrderService的实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String PARKLOT_TYPE = "parklotType";
    private static final String ORDER_STATE = "state";

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponOrderParkingBRepository couponOrderParkingBRepository;
    @Autowired
    private ConfigurationFiles configurationFiles;


    @Autowired
    private PlateRepository plateRepository;
    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private OrderWithdrawMapper orderWithdrawMapper;

    @Autowired
    private ApplyInvoiceMapper applyInvoiceMapper;

    @Autowired
    private OrderWithdrawRepository orderWithdrawRepository;

    @Autowired
    private BankCardRepository bankCardRepository;

    @Autowired
    private UserWebRespository userWebRespository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private RankUserRepository rankUserRepository;

    @Autowired
    private ApplyInvoiceRepository applyInvoiceRepository;

    @Autowired
    private OrderTotalRepository orderTotalRepository;

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    @Autowired
    private FeeRuleService feeRuleService;

    private QueryFunction<OrderParking,OrderRequest> queryFunction;


    private final  String order_file_path="order/";
    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(orderParkingRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }


    @Override
    public Resp withdrawOne(Integer id) {
        OrderWithdraw orderWithdraw=orderWithdrawRepository.findOne(id);
        OrderWithdrawData orderWithdrawData=new OrderWithdrawData();
        orderWithdrawData.setId(orderWithdraw.getId());
        UserMobile userMobile=userMobileRepository.findOne(orderWithdraw.getMobileUserId());
        if(userMobile!=null){
          BalanceUser balanceUser =balanceUserRepository.
                  findByMobileUserIdAndState(userMobile.getId(),Status.Common.VALID.getInt());
          if(balanceUser!=null){
              orderWithdrawData.setBalanceEarn(balanceUser.getBalanceEarn());
          }
          if(userMobile.getUserRankId()!=null){
              RankUser rankUser=rankUserRepository.findOne(userMobile.getUserRankId());
              if(rankUser!=null){
                  orderWithdrawData.setUserLevelId(rankUser.getId()+"");
              }else{
                  orderWithdrawData.setUserLevelId("无");
              }
          }

        }
        if(userMobile!=null){
            orderWithdrawData.setPhone(userMobile.getPhone());
        }
        orderWithdrawData.setBalance(orderWithdraw.getBalance());
        BankCard bankCard=bankCardRepository.findOne(orderWithdraw.getBankCardId());
        if(bankCard!=null){
            orderWithdrawData.setAccountInfo("户名:"+bankCard.getName()+"账户:"+bankCard.getBankNumber());
        }
        orderWithdrawData.setApplyTime(orderWithdraw.getApplyTime());
        orderWithdrawData.setCompleteTime(orderWithdraw.getCompleteTime());
        orderWithdrawData.setMessage(orderWithdraw.getMessage());
        orderWithdrawData.setModePayment("银行");
        orderWithdrawData.setState(orderWithdraw.getState());
        if(orderWithdraw.getWebUserId()==null){
            orderWithdrawData.setUserWebName("暂无");
        }else{
            UserWeb userWeb=userWebRespository.findOne(orderWithdraw.getWebUserId());
            orderWithdrawData.setUserWebName(userWeb.getName());
        }
        File file=null;
        if(orderWithdraw.getFileId()!=null){
            file=fileRepository.findOne(orderWithdraw.getFileId());
        }
        if(file!=null && StringUtils.isNotEmpty(file.getPath())){
            orderWithdrawData.setFilePath(configurationFiles.getPicPath()+file.getPath());
        }
        return RespUtil.successResp(orderWithdrawData);
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp<AbstractPaged<OrderParking>> orderPage(OrderRequest orderRequest) {
        return  queryFunction.query(orderRequest,parkingOrderWhere(orderRequest));
    }

    private Specification<OrderParking> parkingOrderWhere(OrderRequest orderRequest) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root,query,cb);
            // 订单号
            val uselessReserve = orderParkingRepository.findReserveIdFinished();
            page.notIn("id",uselessReserve);
            if(StringUtils.isNotEmpty(orderRequest.getSerialNumber())){
                val idList = orderParkingRepository.findBySerialNumberLike("%"+orderRequest.getSerialNumber()+"%");
                if (idList!=null && idList.size()>0){
                    page.predicates.add(cb.or(cb.like(root.get("serialNumber"), "%" + orderRequest.getSerialNumber() + "%"),root.get("reserveId").in(idList)));
                }else {
                    page.like("serialNumber", orderRequest.getSerialNumber());
                }
            }
            // 停车区名称
            String parklotName = orderRequest.getParklotName();
            if (null != parklotName && !"".equals(parklotName)) {
                val parkingIds = parklocRepository.findByParklotName("%" + parklotName + "%");
                page.in("parklocId", parkingIds);
            }
            // 停车区类型
            Integer parklotType = orderRequest.getParklotType();
            if (null != parklotType) {
                val parkingIds = parklocRepository.findIdsByParklotType(parklotType);
                page.in("parklocId", parkingIds);
            }
            // 车主手机号(phone)
            if(StringUtils.isNotEmpty(orderRequest.getPhone())) {
                val userIds=userMobileRepository.findByPhoneIds("%"+orderRequest.getPhone()+"%");
                page.in("mobileUserId", userIds);
            }
            // 业主手机号(parkingPhone)
            String parkingPhone = orderRequest.getParkingPhone();
            if(StringUtils.isNotEmpty(parkingPhone)) {
                val parkingIds = parklocRepository.findIdsByPhoneLike("%" + parkingPhone + "%");
                page.in("parklocId", parkingIds);
            }
            // 车牌号
            if(StringUtils.isNotEmpty(orderRequest.getPlateNumber())) {
                val plateIds=plateRepository.findPlateByNumber(orderRequest.getPlateNumber(),Status.Common.VALID.getInt());
                page.in("plateId", plateIds);
            }
            // 支付码
            if(StringUtils.isNotEmpty(orderRequest.getTradeNo())) {
                page.like("tradeNo", orderRequest.getTradeNo());
            }
            // 创建时间
            if(orderRequest.getStartCreateTime()!=null && orderRequest.getEndCreateTime()!=null) {
                page.between("createTime", orderRequest.getStartCreateTime(), orderRequest.getEndCreateTime());
            }
            if (orderRequest.getStateList() != null && orderRequest.getStateList().size()!= 0) {
                page.in("state", orderRequest.getStateList());
            }
            return page.pridect();
        };
    }

    @Override
    public Resp one(Integer id) {
        OrderData orderData = new OrderData();
        OrderParking parking = orderParkingRepository.findOne(id);
        if(parking==null) {
            return RespUtil.errorResp(Status.WebErr.ILLEGAL_ALL.getCode(), Status.WebErr.ILLEGAL_ALL.getMsg());
        }
        setCouponCode(parking);
        if(parking.getReserveId()!=null){
            OrderParking reserve = orderParkingRepository.findOne(parking.getReserveId());
            setCouponCode(reserve);
            BeanUtil.converJavaBean(reserve,orderData.getReservation());
            BeanUtil.converJavaBean(parking,orderData.getParking());
        }else {
            BeanUtil.converJavaBean(parking,orderData.getReservation());
        }
        parking = transientProperty(parking);
        if(parking.getReserveId()!=null) {
            System.out.println(parking);
            orderData.getParking().setOvertimeFee(parking.getOvertimeFee());
            orderData.getParking().setTotalFee(parking.getTotalFee());
            orderData.getParking().setParkingFee(parking.getParkingFee());
        }

//        BeanUtil.converJavaBean(parking,orderData.getParking());
//        orderData.getParking().setTotalFee(parking.getOvertimeFee());
        orderData.getReservation().setStartTime(parking.getStartTime());
        orderData.getReservation().setEndTime(parking.getEndTime());
        BeanUtil.converJavaBean(parking, orderData);
        BeanUtil.converJavaBean(parking, orderData.getFinance());
        return RespUtil.successResp(orderData);
    }

    private OrderParking transientProperty(OrderParking orderParking){
        if (null == orderParking) {
            return null;
        }
        Reservation reservation = reservationRepository.findOne(orderParking.getReservationId());
        if (null != reservation) {
            orderParking.setStartTime(reservation.getStartTime());
            orderParking.setEndTime(reservation.getEndTime());
        }
        Plate plate = plateRepository.findOne(orderParking.getPlateId());
        if (null != plate) {
            orderParking.setPlateNumber(plate.getNumber());
        }
        // 获取停车场
        Parkloc parkloc = parklocRepository.findOne(orderParking.getParklocId());
        if (null != parkloc) {
            orderParking.setParkingNumber(parkloc.getNumber());
            UserMobile userMobile = userMobileRepository.findOne(orderParking.getMobileUserId());
            UserMobile userMobileOwner = userMobileRepository.findOne(parkloc.getMobileUserId());
            if (null != userMobile) {
                orderParking.setPhone(userMobile.getPhone());
            }
            if (null != userMobileOwner) {
                orderParking.setParkingPhone(userMobileOwner.getPhone());
            }
            Parklot parklot = parklotRepository.findOne(parkloc.getParklotId());
            if (null == parklot) {
                return orderParking;
            }
            orderParking.setParklotName(parklot.getName());
            orderParking.setParklotType(parklot.getType());
            orderParking.setParklotId(parklot.getId());
            orderParking.setStateStr(Status.OrderParking.find(orderParking.getState()));
            orderParking.setParklotTypeStr(Status.ParklotType.find(parklot.getType()));
            orderParking.setInnershare(parklot.getInnershare());
            orderParking.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
            if (Constants.PARK_LOT_CHARGE_ONE.equals(parklot.getChargeType())) {
                orderParking.setChargeTypeStr("车锁订单");
            }
            orderParking.setChargeType(parklot.getChargeType());
        }

        //预约订单数据整合
        orderParking.setParkingFee(orderParking.getTotalFee());
        if(orderParking.getReserveId()!=null){
            OrderParking orderReserve = orderParkingRepository.findOne(orderParking.getReserveId());
            orderParking.setReservationFee(orderReserve.getTotalFee());
            if(orderParking.getPayChannel()!=null){
                orderParking.setPayChannelStr(!orderReserve.getPayChannel().equals(orderParking.getPayChannel())
                        ?(Status.Channel.find(orderReserve.getPayChannel())+"/"+Status.Channel.find(orderParking.getPayChannel()))
                        :Status.Channel.find(orderReserve.getPayChannel()));
            }else {
                orderParking.setPayChannelStr(Status.Channel.find(orderReserve.getPayChannel()));

            }
            orderParking.handelReserve(orderReserve);
        }else {
            orderParking.setReservationFee(orderParking.getTotalFee());
        }

        setCouponCode(orderParking);
        //　实时计算费用
        if(orderParking.getRealStartTime()!=null) {
            feeRuleService.calculateFee(orderParking);
        }
        if(orderParking.getState().equals(Status.OrderParking.USED.getInt())){
            if(orderParking.getRealStartTime()==null){
                orderParking.setRealStartTime(0L);
            }
            orderParking.setStoptimeStr(TimeUtil.timeStampToHms(System.currentTimeMillis() - orderParking.getRealStartTime()));
        }
        //处理超时时间与费用
        if(orderParking.getOvertime()!=null && orderParking.getOvertime() > 0L){
            orderParking.setOvertimeStr(TimeUtil.timeStampToHms(orderParking.getOvertime()));
        }
        if (orderParking.getOvertimeFee().compareTo(Constants.BIGDECIMAL_ZERO) > 0 ){
            orderParking.setTotalFee(orderParking.getTotalFee().add(orderParking.getOvertimeFee()));
        }
//        //设置支付金额
//        orderParking.setRealFee(
//                orderParking.getTotalFee().subtract(orderParking.getDiscountFee()).compareTo(Constants.BIGDECIMAL_ZERO)>0?
//                        orderParking.getTotalFee().subtract(orderParking.getDiscountFee()):Constants.BIGDECIMAL_ZERO);
        if(orderParking.getState().equals(Status.OrderParking.PAID.getInt()) || orderParking.getState().equals(Status.OrderParking.UNPAID.getInt())){
            if( orderParking.getRealStartTime()!=null && orderParking.getRealEndTime()!=null){
                orderParking.setStoptimeStr(TimeUtil.timeStampToHms(orderParking.getRealEndTime() - orderParking.getRealStartTime()));
            }
        }
        return orderParking;
    }

    private void refundTransientProperty(OrderRefund orderRefund){

    }

    @Override
    @EnableTenantFilter
    public Resp excel(OrderRequest orderRequest, OutputStream outputStream) throws IOException {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        List<OrderParking> dataList = orderParkingRepository.findAll(
                parkingOrderWhere(orderRequest),sort);
        if (dataList.size() == Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        List<OrderParking> copyOrder = new ArrayList<>();
        dataList.forEach(orderParking -> {
            val copy = new OrderParking();
            BeanUtil.converJavaBean(orderParking, copy);
            transientProperty(copy);
            copyOrder.add(copy);
        });
        ExcelUtil<OrderParking> excelUtil = new ExcelUtil<>(outputStream,OrderParking.class);
        excelUtil.setPropertyFormat(this::excelFormat);
        excelUtil.write(copyOrder);
        return  RespUtil.successResp();
    }

    @Override
    public Resp<AbstractPaged<OrderWithdrawData>> withdrawQuery(OrderWithdrawRequest request){
        List<OrderWithdrawData> dataList = orderWithdrawMapper.orderPage(request);
        Integer count = orderWithdrawMapper.orderCount(request);
        AbstractPaged<OrderWithdrawData> data = AbstractPaged.<OrderWithdrawData>builder()
                .sEcho(request.getSEcho()+1)
                .dataList(dataList)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .build();
        return RespUtil.successResp(data);
    }

    @Override
    public Resp<AbstractPaged<ApplyInvoice>> invoiceQuery(ApplyInvoiceRequest request){
        List<ApplyInvoice> dataList = applyInvoiceMapper.applyPage(request);
        Integer count = applyInvoiceMapper.applyCount(request);
        AbstractPaged<ApplyInvoice> data = AbstractPaged.<ApplyInvoice>builder()
                .sEcho(request.getSEcho()+1)
                .dataList(dataList)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .build();
        return RespUtil.successResp(data);
    }

    @Transactional
    @Override
    public Resp clsedOrderWithdraw(OrderWithdrawRequest orderWithdrawRequest) {
        OrderWithdraw orderWithdraw=orderWithdrawRepository.findOne(orderWithdrawRequest.getId());
        orderWithdraw.setState(orderWithdrawRequest.getState());
        orderWithdraw.setWebUserId(orderWithdrawRequest.getWebUserId());
        orderWithdraw.setMessage(orderWithdrawRequest.getMessage());
        orderWithdraw.setCompleteTime(System.currentTimeMillis());
        String path="";
        String fileName= System.currentTimeMillis()+"";
        try {
            path=FileUploadUtils.uploadImage(configurationFiles.getWebUploadPath()+order_file_path,orderWithdrawRequest.getFilePath(),fileName);
        } catch (Exception e) {
            throw new ExcelException(Status.WebErr.SYSTEM_ERROR.getCode(),Status.WebErr.SYSTEM_ERROR.getMsg());
        }
        String suffix = path.substring(path.lastIndexOf("."));
        File file=new File();
        file.setCreateTime(System.currentTimeMillis());
        file.setIntro("支付凭证");
        file.setName(fileName+suffix);
        file.setState(Status.Common.VALID.getInt());
        file.setPath(order_file_path+fileName+suffix);
        file=fileRepository.save(file);
        orderWithdraw.setFileId(file.getId());
        orderWithdrawRepository.save(orderWithdraw);
        orderTotalRepository.updateStateBySerialNumber(orderWithdraw.getSerialNumber(),orderWithdrawRequest.getState());
        if(orderWithdraw.getState().equals(Status.Withdraw.PROCESS_SUCCESS.getInt())){
            balanceUserRepository.withdrawBalanceFrozen(orderWithdraw.getBalance(),orderWithdraw.getMobileUserId(),Status.Common.VALID.getInt());
        }
        if(orderWithdraw.getState().equals(Status.Withdraw.PROCESS_FAILED.getInt())){
            balanceUserRepository.withdrawBalanceEarn(orderWithdraw.getBalance(),orderWithdraw.getMobileUserId(),Status.Common.VALID.getInt());
        }
        return RespUtil.successResp();
    }

    @Override
    public Resp orderWithdrawExcel(OrderWithdrawRequest orderWithdrawRequest, OutputStream outputStream)
            throws IOException {
        List<OrderWithdrawData> orderWithdrawDataList=orderWithdrawMapper.orderWithdrawExcel(orderWithdrawRequest);
        if (orderWithdrawDataList.size() == Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(int i=0;i<orderWithdrawDataList.size();i++){
            String stateStr=Status.Withdraw.find(orderWithdrawDataList.get(i).getState());
            orderWithdrawDataList.get(i).setStateStr(stateStr);
        }
        ExcelUtil<OrderWithdrawData> excelUtil = new ExcelUtil<OrderWithdrawData>(outputStream,OrderWithdrawData.class);
        excelUtil.write(orderWithdrawDataList);
        return RespUtil.successResp();
    }

    @Override
    public Resp invoiceOne(Integer id) {
        ApplyInvoice applyInvoice=applyInvoiceRepository.findOne(id);
        ApplyInvoiceData applyInvoiceData=new ApplyInvoiceData();
        applyInvoiceData.setId(applyInvoice.getId());
        applyInvoiceData.setApplyTime(applyInvoice.getApplyTime());
        applyInvoiceData.setCompleteTime(applyInvoice.getCompleteTime());
        applyInvoiceData.setFee(applyInvoice.getFee());
        applyInvoiceData.setTitle(applyInvoice.getTitle());
        UserMobile userMobile=userMobileRepository.findOne(applyInvoice.getMobileUserId());
        applyInvoiceData.setPhone(userMobile.getPhone());
        applyInvoiceData.setType(applyInvoice.getType());
        applyInvoiceData.setState(applyInvoice.getState());
        applyInvoiceData.setFileId(applyInvoice.getFileId());
        if(applyInvoice.getFileId()!=null){
            File file=fileRepository.findOne(applyInvoice.getFileId());
            if(StringUtils.isNotEmpty(file.getPath())){
                applyInvoiceData.setPath(configurationFiles.getPicPath()+file.getPath());
            }
        }
        BalanceUser balanceUser=balanceUserRepository.findByMobileUserIdAndState(applyInvoice.getMobileUserId(),Status.Common.VALID.getInt());
        if(balanceUser!=null){
            applyInvoiceData.setTotalFee(balanceUser.getBalanceInvoice());
        }else{
            applyInvoiceData.setTotalFee(new BigDecimal(0));
        }
        return RespUtil.successResp(applyInvoiceData);
    }

    @Override
    public Resp orderInvoiceExcel(ApplyInvoiceRequest request, OutputStream outputStream) throws IOException {
        List<ApplyInvoice> applyInvoiceList=applyInvoiceMapper.orderInvoiceExcel(request);
        if (applyInvoiceList.size() == Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(int i=0;i<applyInvoiceList.size();i++){
            String stateStr=Status.Apply.find(applyInvoiceList.get(i).getState());
            String typeStr=Status.ApplyType.find(applyInvoiceList.get(i).getType());
            applyInvoiceList.get(i).setStateStr(stateStr);
            applyInvoiceList.get(i).setTypeStr(typeStr);
        }
        ExcelUtil<ApplyInvoice> excelUtil = new ExcelUtil<ApplyInvoice>(outputStream,ApplyInvoice.class);
        excelUtil.write(applyInvoiceList);
        return RespUtil.successResp();
    }

    private void setCouponCode(OrderParking orderParking){
//        orderParking.setParkingFee(orderParking.getTotalFee());
        List<String> couponCodeList = couponRepository.getCouponCodes(orderParking.getId());
        if (couponCodeList.size() > 0) {
            String couponCodeStr = couponCodeList.toString();
            String couponCodes = couponCodeStr.substring(couponCodeStr.indexOf('[') + 1, couponCodeStr.indexOf(']'));
            orderParking.setCouponCodes(couponCodes);
            if(!Status.Channel.COUPON.getValue().equals(orderParking.getPayChannel())) {
                orderParking.setPayChannelStr(Status.Channel.find(orderParking.getPayChannel())+"/优惠券抵扣");
            }else {
                orderParking.setPayChannelStr("优惠券抵扣");
            }
        }else {
            orderParking.setPayChannelStr(Status.Channel.find(orderParking.getPayChannel()));
        }
    }

    private Map<String, Object> excelFormat(Map<String, Object> map){
        OrderParking parking = orderParkingRepository.findOne((Integer) map.get("id"));
        setCouponCode(parking);
        OrderParking reserve;
        if(parking.getReserveId()!=null){
            reserve = orderParkingRepository.findOne(parking.getReserveId());
            setCouponCode(reserve);
            map.put("discountFeePa",parking.getDiscountFee());
            map.put("payChannelStrPa",parking.getPayChannelStr());
            map.put("couponCodesPa", parking.getCouponCodes());
            map.put("payFeePa", parking.getRealFee());
            map.put("tradeNoPa",parking.getTradeNo());
            map.put("tripartiteFeePa",parking.getTripartiteFee());
        }else {
            reserve = parking;
        }
        map.put("discountFeeRe",reserve.getDiscountFee());
        map.put("payChannelStrRe",reserve.getPayChannelStr());
        map.put("couponCodesRe", reserve.getCouponCodes());
        map.put("payFeeRe", reserve.getRealFee());
        map.put("tradeNoRe",reserve.getTradeNo());
        map.put("tripartiteFeeRe",reserve.getTripartiteFee());
        return map;
    }

}
