package com.qhieco.apiservice.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.google.gson.Gson;
import com.qhieco.apiservice.BarrierApiService;
import com.qhieco.apiservice.PayService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.apiservice.impl.barrier.BoshiBarrierService;
import com.qhieco.apiservice.impl.wx.WxSendTemplateService;
import com.qhieco.barrier.boostedgoal.request.BoostedGoalParkingRequest;
import com.qhieco.barrier.keytop.request.KeyTopParkingRequest;
import com.qhieco.barrier.keytop.response.KeyTopParkingCostRespone;
import com.qhieco.barrier.keytop.response.KeyTopParkingPayCostRespone;
import com.qhieco.barrier.keytop.response.KeyTopParkingResponse;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.FeeRuleParkingMapper;
import com.qhieco.mapper.OrderMapper;
import com.qhieco.mapper.ParklotParamsMapper;
import com.qhieco.request.api.PayRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.OrderParkingData;
import com.qhieco.response.data.api.PayRepData;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:38
 * <p>
 * 类说明：
 * 预约service实现类
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    FeeRuleReserveRepository feeRuleReserveRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponOrderParkingBRepository couponOrderParkingBRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    BalanceUserRepository balanceUserRepository;

    @Autowired
    OrderTotalRepository orderTotalRepository;

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private BarrierApiService barrierApiService;

    @Autowired
    private ConfigurationFiles configuration;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    private WxSendTemplateService wxSendTemplateService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BoshiBarrierService boshiBarrierService;

    @Autowired
    private OrderParkingScanpayRepository orderParkingScanpayRepository;

    @Autowired
    private FeeRuleParkingMapper feeRuleParkingMapper;

    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    private FeeRuleParkingMapper feeRuleMapper;

    private static String randomStr = AlphabetRandom.random(32);

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp pay(PayRequest payRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(payRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Long now = System.currentTimeMillis();
        Integer orderId = payRequest.getOrder_id();
        //检查订单是否存在
        OrderParking orderParking = orderParkingRepository.findOne(orderId);
        if (null == orderParking) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
        }
        //检查订单是否是待支付状态
        Integer orderState = orderParking.getState();
        Boolean isPayState = orderState.equals(Status.OrderParking.UNCONFIRMED.getInt()) || orderState.equals(Status.OrderParking.UNPAID.getInt()) || orderState.equals(Status.OrderParking.USED.getInt());
        if (!isPayState) {
            throw new QhieException(Status.ApiErr.NOT_PAY_ORDER);
        }
        //计算实付金额
        BigDecimal realFee;
        BigDecimal totalFee;
        if(orderState.equals(Status.OrderParking.UNCONFIRMED)){
            totalFee = orderParking.getTotalFee();
        }else{
            Integer parkingFeePaidFlag = orderParking.getParkingFeePaidFlag();
            if(null != parkingFeePaidFlag && parkingFeePaidFlag == 1){
                totalFee = orderParking.getOvertimeFee();
            }else{
                totalFee = orderParking.getOvertimeFee().add(orderParking.getTotalFee());
            }
        }
        Integer couponId = payRequest.getCoupon_id();
        log.info("计算支付费用金额，totalFee = " + totalFee + ", couponId =" + couponId + ", orderParking=" + orderParking);
        //如果优惠券Id是空或者零删除订单关联的优惠券Id
        if (null == couponId || Constants.MIN_NON_NEGATIVE_INTEGER == couponId) {
            couponOrderParkingBRepository.updateStateByOrderId(orderId,Status.Common.INVALID.getInt(),now);
            realFee = totalFee;
        } else {
            BigDecimal couponLimit = couponRepository.findCouponLimitByCouponId(couponId, Status.Coupon.COUPON_CONVERTIBILITY.getInt());
            if (null == couponLimit) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_COUPON);
            }
            if (couponLimit.compareTo(totalFee) >= 0) {
                log.info("优惠券金额大于支付金额， 全部使用优惠券支付：" + couponLimit);
                realFee = Constants.BIGDECIMAL_ZERO;
            } else {

                realFee = totalFee.subtract(couponLimit).setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
                log.info("优惠券金额小于支付金额，需要补差价：actualFee = " + realFee);
            }
        }
        PayRepData payRepData = null;
        //如果实际支付金额为0，直接改成已支付。
        if (Constants.BIGDECIMAL_ZERO.compareTo(realFee) == Constants.MIN_NON_NEGATIVE_INTEGER) {
            Integer parklocId = orderParking.getParklocId();
            Integer parklotId = orderParking.getParklotId();
            log.info("实际支付金额为0,orderParkingId:{}", orderId);
            Integer payChannel;
            BigDecimal platformIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal ownerIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal manageIncome = Constants.BIGDECIMAL_ZERO;
            //如果totalFee不为零  支付渠道为优惠券支付
            if (totalFee.compareTo(Constants.BIGDECIMAL_ZERO) == Constants.MIN_NON_NEGATIVE_INTEGER) {
                payChannel = Constants.PAY_CHANNEL_NONE;
            } else {
                payChannel = Constants.PAY_CHANNEL_COUPON;
                //如果是优惠券支付 分成优惠券
                //判断业主是否是管理员
                UserMobile userMobile = userMobileRepository.findByParklocId(parklocId);
                if(null == userMobile){
                    throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
                }
                if(Constants.PARKING_ADMIN == userMobile.getType()){
                    String managePercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
                    if (StringUtils.isEmpty(managePercentage)) {
                        managePercentage = Constants.PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT;
                    }
                    manageIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(managePercentage));
                    platformIncome = NumberUtils.getPlatformFeeCeil(totalFee, ownerIncome, manageIncome);
                }else{
                    String ownerPercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.OWNER_PERCENTAGE, Status.Common.VALID.getInt());
                    if (StringUtils.isEmpty(ownerPercentage)) {
                        ownerPercentage = Constants.OWNER_PERCENTAGE_DEFAULT;
                    }
                    String propcomPpercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_PERCENTAGE, Status.Common.VALID.getInt());
                    if (StringUtils.isEmpty(propcomPpercentage)) {
                        propcomPpercentage = Constants.PROPCOMP_PERCENTAGE_DEFAULT;
                    }
                    ownerIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(ownerPercentage));
                    manageIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(propcomPpercentage));
                    platformIncome = NumberUtils.getPlatformFeeCeil(totalFee, ownerIncome, manageIncome);
                }

            }
            //更新订单状态
            if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderState)) {
                log.info("订单状态改为已预约,orderParkingId:{}", orderId);
                //置为已预约
                orderParkingRepository.payReserve(realFee, Constants.BIGDECIMAL_ZERO, payChannel, platformIncome, ownerIncome, manageIncome, now, Status.OrderParking.RESERVED.getInt(), orderId);
                orderTotalRepository.updateStateBySerialNumber(orderParking.getSerialNumber(), Status.OrderParking.RESERVED.getInt());
                Parklot parklot=parklotRepository.findOne(orderParking.getParklotId());
                if(parklot!=null && Constants.PARK_LOT_CHARGE_TYPE_ZEOR.equals(parklot.getChargeType())){
                    //添加零时门禁
                    registeredTemp(orderParking);
                }
            } else if (Status.OrderParking.UNPAID.getInt().equals(orderState)  || orderState.equals(Status.OrderParking.USED.getInt())) {
                log.info("订单状态改为已支付,orderParkingId:{}", orderId);
                //置为已支付
                orderParkingRepository.payParking(realFee, Constants.BIGDECIMAL_ZERO, payChannel,  platformIncome, ownerIncome, manageIncome, now, Status.InvoiceStatus.UNMAKE.getInt(), Status.OrderParking.PAID.getInt(), orderId);
                orderTotalRepository.updateStateBySerialNumber(orderParking.getSerialNumber(), Status.OrderParking.PAID.getInt());
                Integer reserveOrderId = orderParking.getReserveId();
                //查询停车订单对应的预约订单
                OrderParking reserveOrder = orderParkingRepository.findOne(reserveOrderId);
                //计算车位所有者应有分成
                BigDecimal ownerIncomeTotal = new BigDecimal(decimalFormat.format(reserveOrder.getOwnerIncome().add(ownerIncome)));
                //更新车位所有者分成
                balanceUserRepository.updateBalanceEarnByParklocId(parklocId, ownerIncomeTotal);
                //计算车场分成
                BigDecimal manageIncomeTotal = new BigDecimal(decimalFormat.format(reserveOrder.getOwnerIncome().add(ownerIncome)));
                balanceParklotRepository.updateBalanceByParklotId(parklotId, manageIncomeTotal);
                //更改停车订单对应的预约订单
                orderParkingRepository.updateInvoiceState(reserveOrderId,Status.InvoiceStatus.UNMAKE.getInt());
                //更新用户可开票金额
                balanceUserRepository.updateBalanceInvoice(reserveOrder.getRealFee(),reserveOrder.getMobileUserId());
            }
            //更新优惠券状态
            couponRepository.updateStateAndUseTimeAndUseMoney(orderId, Status.Coupon.USED.getInt(), now, totalFee);
            payRepData = new PayRepData();
            payRepData.setIsZero(1);
            return RespUtil.successResp(payRepData);
        }
        Integer channel = payRequest.getChannel();
        Integer type = payRequest.getType();
        String wapUrl = payRequest.getWapUrl();
        String spbillCreateIp = payRequest.getSpbillCreateIp();
        String serialNumber = OrderNoGenerator.getOrderNo(Constants.RESERVATION_ORDER, orderParking.getMobileUserId().toString());
        //更新订单总表编号
        orderTotalRepository.updateSerialNumberByOrderId(serialNumber, orderId);
        //更新订单编号
        orderParkingRepository.updateSerialNumber(orderId, serialNumber);
        String outTradeNo = serialNumber;
        //更新支付渠道
        orderParkingRepository.updatePayChannel(orderId, channel);
        if (channel.equals(Constants.PAY_CHANNEL_ALIPAY)) {
            String orderInfo;
            if (type == null || !type.equals(Constants.PAY_TYPE_HTML5)) {
                orderInfo = this.alipayTradeAppPay(realFee, outTradeNo);
            } else {//HTML5
                orderInfo = this.alipayTradeHTML5Pay(realFee, outTradeNo, wapUrl);
            }
            payRepData = new PayRepData(orderInfo, null, null, null, null, null, null, null);
        } else if (channel.equals(Constants.PAY_CHANNEL_WXPAY) || channel.equals(Constants.PAY_CHANNEL_WXPAY_PUBLIC) || channel.equals(Constants.PAY_CHANNEL_WXPAY_XCX)) {
            String body = null;
            if (orderState.equals(Status.OrderParking.UNCONFIRMED.getInt())) {
                body = Constants.RESERVE_BODY;
            } else if (orderState.equals(Status.OrderParking.UNPAID.getInt()) || Status.OrderParking.USED.getInt().equals(orderState)) {
                body = Constants.PARKING_BODY;
            }
            String response = "";
            if (type == null || (!type.equals(Constants.PAY_TYPE_HTML5) && !type.equals(Constants.PAY_CHANNEL_WXPAY_XCX) ) ) {
                response = this.unifiedorder(realFee, body, type, outTradeNo);
            } else {
                log.info("调用微信H5支付接口");
                String openId = "";
                if(type.equals(Constants.PAY_CHANNEL_WXPAY_XCX)){
                    openId=payRequest.getOpenId();
                }else {
                    if (StringUtils.isEmpty(payRequest.getOpenId())) {
                        openId = userExtraInfoRepository.findByWxBindOpenId(orderParking.getMobileUserId());
                    } else {
                        openId = payRequest.getOpenId();
                    }
                }
                response = this.officialAccountsPay(realFee, body, outTradeNo, wapUrl, spbillCreateIp, openId,type);
                WxpayData wxpayData = (WxpayData) Xml.fromXML(response);
                String returnCode = wxpayData.getReturnCode();
                if (!returnCode.equals(Constants.WXPAY_RETURN_CODE_SUCCESS)) {
                    log.error("wxpay unifiedorder failed.");
                    throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
                }
                if (!this.verify(wxpayData)) {
                    log.error("verify failed.");
                    throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
                }
                String timeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                String paySign = "appId=" + wxpayData.getAppid() +
                        "&nonceStr=" + wxpayData.getNonceStr() +
                        "&package=prepay_id=" + wxpayData.getPrepayId() +
                        "&signType=MD5" +
                        "&timeStamp=" + timeStamp;
                paySign = Signature.getSign(paySign, Constants.WXPAY_KEY);
                PayRepData payRepData1 = new PayRepData();
                payRepData1.setAppId(wxpayData.getAppid());
                payRepData1.setNonceStr(wxpayData.getNonceStr());
                payRepData1.setTimeStamp(timeStamp);
                payRepData1.setPrepayId(wxpayData.getPrepayId());
                payRepData1.setSign(paySign);
                return RespUtil.successResp(payRepData1);
            }
            WxpayData wxpayData = (WxpayData) Xml.fromXML(response);
            String returnCode = wxpayData.getReturnCode();
            if (!returnCode.equals(Constants.WXPAY_RETURN_CODE_SUCCESS)) {
                log.error("wxpay unifiedorder failed.");
                throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
            }
            if (!this.verify(wxpayData)) {
                log.error("verify failed.");
                throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
            }
            payRepData = this.getPayData(wxpayData);
        }
        log.info("pay successfully, orderId: {}, channel: {}, fee: {}, userCouponId: {}", orderId, channel, totalFee, couponId);
        log.info("pay支付方法返回数据，" + payRepData);
        return RespUtil.successResp(payRepData);
    }

    /**
     * 临时卡登记功能
     *
     * @param orderParking 预约订单
     */
    private void registeredTemp(OrderParking orderParking) {
        try {
            log.info("零时卡登记orderParkingId:{}", orderParking.getId());
            Integer parklotId = orderParking.getParklotId();
            //查询车场在对应表中的Id
            Parklot parklot = parklotRepository.findOne(parklotId);
            Integer barrierManufacturer = parklot.getBarrierManufacturer();
            String extraParklotId = parklot.getExtraParklotId();
            log.info("barrierManufacturer:{},extraParklotId:{}", barrierManufacturer, extraParklotId);
            if (Constants.BARRIER_MANUFACTURER_BOOSTED_GOAL.equals(barrierManufacturer)) {
                log.info("进入boostedGoal添加白名单");
                this.boostedGoalRegistered(extraParklotId, orderParking);
            } else if (Constants.BARRIER_MANUFACTURER_KEY_TOP.equals(barrierManufacturer)) {
                log.info("进入keyTop添加白名单");
                this.keyTopRegistered(extraParklotId, orderParking);
            }
        } catch (Exception e) {
            log.error("临时卡登记异常，" + e);
        }
    }


    private void boostedGoalRegistered(String extraParklotId, OrderParking orderParking) {
        Integer plateId = orderParking.getPlateId();
        Integer parklotId = orderParking.getParklotId();
        Plate plate = plateRepository.findOne(plateId);
        Long now = System.currentTimeMillis();
        List<String> params = new ArrayList<>();
        String accessCode = configuration.getBoostedGoalAccessCode();
        String plateNumber = plate.getNumber();
        Long time=now-600000;
        String beginDate = TimeUtil.timestampToStr(time);
        params.add("AccessCode=" + accessCode);
        params.add("BusinessCode=" + "RP0001");
        params.add("SignType=" + "SHAONE");
        params.add("PlateNumber=" + plateNumber);

        log.info("extraParklotId:{}", extraParklotId);
        params.add("ParkingID=" + extraParklotId);
        params.add("BeginDate=" + beginDate);
        //查询车场最长预留时间
        String value = parklotParamsBRepository.findValueByParklotId(parklotId, Constants.MAX_DELAY_TIME, Status.Common.VALID.getInt());
        if (StringUtils.isEmpty(value)) {
            value = Constants.MAX_DELAY_TIME_DEFAULT;
        }
        Long maxDelayTime = TimeUtil.minutesToMilliSeconds(Integer.valueOf(value));
        //查询预约开始时间
        Reservation reservation = reservationRepository.findOne(orderParking.getReservationId());
        String endDate = TimeUtil.timestampToStr(maxDelayTime + reservation.getStartTime());
        params.add("EndDate=" + endDate);
        params.add("Secretkey=" + configuration.getBoostedGoalSecretKey());
        String sign = EncryptUtil.signature(params);
        BoostedGoalParkingRequest request = new BoostedGoalParkingRequest(accessCode, "RP0001", "SHAONE", sign, plateNumber, extraParklotId, beginDate, endDate);
        Gson gson = new Gson();
        String json = "data=" + gson.toJson(request);
        String url = configuration.getBoostedGoalParkingUrl();
        barrierApiService.registeredTemp(json, url, Constants.APPLICATION_FORM);
    }


    private void keyTopRegistered(String extraParklotId, OrderParking orderParking) {
        Integer plateId = orderParking.getPlateId();
        Integer parklotId = orderParking.getParklotId();
        Integer userId = orderParking.getMobileUserId();
        Plate plate = plateRepository.findOne(plateId);
        Long now = System.currentTimeMillis();
        String appid = configuration.getKeyTopAppId();
        String keyTopSecretKey = configuration.getKeyTopSecretKey();
        String keyTopParkingUrl = configuration.getKeyTopParkingUrl();
        //查询车场最长预留时间
        String value = parklotParamsBRepository.findValueByParklotId(parklotId, Constants.MAX_DELAY_TIME, Status.Common.VALID.getInt());
        if (StringUtils.isEmpty(value)) {
            value = Constants.MAX_DELAY_TIME_DEFAULT;
        }
        Long maxDelayTime = TimeUtil.minutesToMilliSeconds(Integer.valueOf(value));
        //查询预约开始时间
        Reservation reservation = reservationRepository.findOne(orderParking.getReservationId());
        String enterTime = TimeUtil.stampToDate(now);
        //失效时间
        String failureTime = TimeUtil.stampToDate(maxDelayTime + reservation.getStartTime());
        //查询用户信息
        UserMobile userMobile = userMobileRepository.findOne(userId);
        String tel = userMobile.getPhone();
        Integer type = Constants.MIN_NON_NEGATIVE_INTEGER;
        String key = EncryptUtil.md5Password(extraParklotId + plate.getNumber() + type + enterTime + tel + tel + failureTime + TimeUtil.timestampToStr2(now) + keyTopSecretKey);
        KeyTopParkingRequest request = new KeyTopParkingRequest(Integer.valueOf(appid), key, Integer.valueOf(extraParklotId), plate.getNumber(), type, enterTime, tel, tel, failureTime);
        Gson gson = new Gson();
        String json = gson.toJson(request);
        ResponseEntity<String> response = barrierApiService.registeredTemp(json, keyTopParkingUrl, Constants.APPLICATION_JSON);
        KeyTopParkingResponse keyTopParkingResponse = new Gson().fromJson(response.getBody(), KeyTopParkingResponse.class);
        log.info("解析后keyTopParkingResponse：{}", keyTopParkingResponse);
        //返回的订单Id存入数据库
        if (null != keyTopParkingResponse) {
            if (keyTopParkingResponse.getData().size() > 0) {
                String orderNo = keyTopParkingResponse.getData().get(Constants.FIRST_INDEX).getOrderNo();
                orderParking.setThirdPartyNo(orderNo);
                if (null == orderParkingRepository.save(orderParking)) {
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
            }

        }
    }


    /**
     * 请求手机端支付宝支付
     *
     * @param payFee
     * @param outTradeNo
     * @return
     */
    public String alipayTradeAppPay(BigDecimal payFee, String outTradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_SERVER_URL, configuration.getAppId(), configuration.getPrivateKey(), Constants.FORMAT_JSON, Constants.ENCODING_FORMAT_UTF_8, configuration.getAlipayPublicKey(), Constants.ALIPAY_SIGN_TYPE);
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel alipayTradeAppPayModel = new AlipayTradeAppPayModel();
        String totalAmount = payFee.toString();
        String httpPrefix = configuration.getHttpPrefix();
        String ip = configuration.getServerIpDev();
        String port = configuration.getPort();
        String notifyUrl = Constants.ALIPAY_NOTIFY_URL;
        log.info("支付宝充值，充值订单号 outTradeNo=" + outTradeNo + ", totalAmount = " + totalAmount + ", notifyUrl = "
                + notifyUrl + "，configuration = " + configuration);
        String alinotifyUrl = httpPrefix.concat(ip).concat(":").concat(port).concat(notifyUrl);
        log.info("支付宝支付回调地址：alinotifyUrl =" + alinotifyUrl);
        alipayTradeAppPayModel.setBody(Constants.BODY);
        alipayTradeAppPayModel.setSubject(Constants.SUBJECT);
        alipayTradeAppPayModel.setOutTradeNo(outTradeNo);
        alipayTradeAppPayModel.setTimeoutExpress(Constants.ALIPAY_TIMEOUT_EXPRESS);
        alipayTradeAppPayModel.setTotalAmount(totalAmount);
        alipayTradeAppPayModel.setProductCode(Constants.ALIPAY_PRODUCT_CODE);
        alipayTradeAppPayRequest.setBizModel(alipayTradeAppPayModel);
        alipayTradeAppPayRequest.setNotifyUrl(alinotifyUrl);
        try {
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(alipayTradeAppPayRequest);
            String body = alipayTradeAppPayResponse.getBody();
            log.info("支付宝支付接口相应数据为body=" + body);
            return body;
        } catch (AlipayApiException e) {
            log.error("alipayTradeAppPay failed." + e);
        }
        return null;
    }


    public String alipayTradeHTML5Pay(BigDecimal payFee, String outTradeNo, String returnUrl) {
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_SERVER_URL, configuration.getAppId(), configuration.getPrivateKey(), Constants.FORMAT_JSON, Constants.ENCODING_FORMAT_UTF_8, configuration.getAlipayPublicKey(), Constants.ALIPAY_SIGN_TYPE);
        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
        String totalAmount = payFee.toString();
        String httpPrefix = configuration.getHttpPrefix();
        String ip = configuration.getServerIpDev();
        String port = configuration.getPort();
        String notifyUrl = Constants.ALIPAY_NOTIFY_URL;
        log.info("支付宝支付，支付订单号 outTradeNo=" + outTradeNo + ", totalAmount = " + totalAmount + ", notifyUrl = "
                + notifyUrl + "，configuration = " + configuration);
        String alinotifyUrl = httpPrefix.concat(ip).concat(":").concat(port).concat(notifyUrl);
        alipayTradeWapPayModel.setBody(Constants.BODY);
        alipayTradeWapPayModel.setSubject(Constants.SUBJECT);
        alipayTradeWapPayModel.setOutTradeNo(outTradeNo);
        alipayTradeWapPayModel.setTimeoutExpress(Constants.ALIPAY_TIMEOUT_EXPRESS);
        alipayTradeWapPayModel.setTotalAmount(totalAmount);
        alipayTradeWapPayModel.setProductCode("QUICK_WAP_PAY");
        alipayRequest.setReturnUrl(returnUrl);
        //在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(alinotifyUrl);
        //填充业务参数
        alipayRequest.setBizModel(alipayTradeWapPayModel);
        try {
            //调用SDK生成表单
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            log.info("支付宝支付接口相应数据为form=" + form);
            return form;
        } catch (AlipayApiException e) {
            log.error("alipayTradeAppPay failed." + e);
        }
        return null;
    }

    public String unifiedorder(BigDecimal payFee, String body, Integer type, String outTradeNo) {
        try {
            log.info("randomStr: {}", randomStr);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HashMap<String, String> paramMap = new HashMap<>(16);
            paramMap.put("httpPrefix", configuration.getHttpPrefix());
            paramMap.put("serverIpDev", configuration.getServerIpDev());
            paramMap.put("port", configuration.getPort());
            paramMap.put("wxAppId", configuration.getWxOpAppId());
            paramMap.put("mchId", configuration.getOpMchId());
            paramMap.put("spbillCreateIp", configuration.getSpbillCreateIp());
            Map<String, String> map = MapUtil.getUnifiedorderMap(outTradeNo, payFee, body, randomStr, paramMap);
            String string = MapUtil.join(map);
            String sign = Signature.getSign(string, Constants.WXPAY_KEY);
            if (sign == null) {
                log.error("sign is null.");
                return null;
            }
            String httpPrefix = configuration.getHttpPrefix();
            String ip = configuration.getServerIpDev();
            String port = configuration.getPort();
            String notifyUrl = Constants.WXPAY_NOTIFY_URL;

            Wxpay wxpay = new Wxpay();
            log.info("type: {}", type);
            wxpay.setAppid(configuration.getWxOpAppId());
            wxpay.setMchId(configuration.getOpMchId());
            wxpay.setBody(body);
            wxpay.setNonceStr(randomStr);
            wxpay.setNotifyUrl(httpPrefix.concat(ip).concat(":").concat(port).concat(notifyUrl));
            wxpay.setOutTradeNo(outTradeNo);
            wxpay.setSpbillCreateIp(configuration.getSpbillCreateIp());
            wxpay.setTotalFee(Money.CNYToChinaFen(payFee));
            wxpay.setTradeType(Constants.WXPAY_TRADE_TYPE);
            wxpay.setSign(sign);
            String xmlStr = Xml.toXMl(wxpay);
            xmlStr = new String(xmlStr.getBytes("UTF-8"), "ISO-8859-1");
            log.info("微信支付请求报文 xmlStr: {}", xmlStr);
            String response = restTemplate.postForObject(Constants.WXPAY_SERVER_URL, xmlStr, String.class);
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            log.info("微信支付相应报文  response: {}", response);
            return response;
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException" + e);
            return null;
        } catch (Exception e) {
            log.error("WxpayService wxpay unifiedorder failed." + e);
            return null;
        }
    }

    public String officialAccountsPay(BigDecimal payFee, String body, String outTradeNo, String wapUrl, String spbillCreateIp, String openId,Integer type) {
        try {
            String notifyUrl = configuration.getHttpPrefix().concat(configuration.getServerIpDev()).concat(":")
                    .concat(configuration.getPort()).concat(Constants.WXPAY_NOTIFY_URL);
            HashMap<String, String> paramMap = new HashMap<>();
            Map<String,String> stringStringMap=initWxInfo(type);
            String appid = stringStringMap.get("appId");
            String mch_id = stringStringMap.get("mchId");
            String sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + wapUrl + "\",\"wap_name\": \"享你了\"}}";
            paramMap.put("appid", appid);
            paramMap.put("mch_id", mch_id);
            paramMap.put("nonce_str", randomStr);
            paramMap.put("body", body);
            paramMap.put("notify_url", notifyUrl);
            paramMap.put("out_trade_no", outTradeNo);
            paramMap.put("spbill_create_ip", spbillCreateIp);
            paramMap.put("total_fee", String.valueOf(Money.CNYToChinaFen(payFee)));
            paramMap.put("trade_type", "JSAPI");
            paramMap.put("openid", openId);
            paramMap.put("scene_info", sceneInfo);
            String string = MapUtil.join(paramMap);
            String sign = Signature.getSign(string, Constants.WXPAY_KEY);
            if (sign == null) {
                log.error("sign is null.");
                return null;
            }
            Wxpay wxpay = new Wxpay();
            wxpay.setAppid(appid);
            wxpay.setMchId(mch_id);
            wxpay.setBody(body);
            wxpay.setNonceStr(randomStr);
            wxpay.setNotifyUrl(notifyUrl);
            wxpay.setOutTradeNo(outTradeNo);
            wxpay.setSpbillCreateIp(spbillCreateIp);
            wxpay.setTotalFee(Money.CNYToChinaFen(payFee));
            wxpay.setTradeType("JSAPI");
            wxpay.setSceneInfo(sceneInfo);
            wxpay.setOpenid(openId);
            wxpay.setSign(sign);
            String xmlStr = Xml.toXMl(wxpay);
            xmlStr = new String(xmlStr.getBytes("UTF-8"), "ISO-8859-1");
            log.info("微信H5支付请求报文 xmlStr: {}", xmlStr);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(Constants.WXPAY_SERVER_URL, xmlStr, String.class);
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            log.info("微信H5支付相应报文  request: {}", response);
            return response;
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException" + e);
            return null;
        } catch (Exception e) {
            log.error("WxpayService wxpay unifiedorder failed." + e);
            return null;
        }
    }

    public Boolean verify(WxpayData wxpayData) {
        String returnCode = wxpayData.getReturnCode();
        String returnMsg = wxpayData.getReturnMsg();
        String appId = wxpayData.getAppid();
        String mchId = wxpayData.getMchId();
        String nonceStr = wxpayData.getNonceStr();
        String sign = wxpayData.getSign();
        if (sign == null) {
            log.error("sign is null.");
            return null;
        }
        String resultCode = wxpayData.getResultCode();
        String prepayId = wxpayData.getPrepayId();
        String tradeType = wxpayData.getTradeType();
        String mwebUrl = wxpayData.getMwebUrl();
        Map<String, String> map = MapUtil.getRetMap(returnCode, returnMsg, appId, mchId, nonceStr, resultCode, prepayId, tradeType, mwebUrl);
        String string = MapUtil.join(map);
        String newSign = Signature.getSign(string, Constants.WXPAY_KEY);
        if (newSign == null) {
            log.error("newSign is null.");
            return null;
        }
        if (newSign.equals(sign)) {
            return true;
        }
        return false;
    }


    public PayRepData getPayData(WxpayData wxpayData) {
        String appId = wxpayData.getAppid();
        String partnerId = wxpayData.getMchId();
        String prepayId = wxpayData.getPrepayId();
        String nonceStr = randomStr;
        String packageValue = Constants.WXPAY_PACKAGE_VALUE;
        String timeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        String mwebUrl = wxpayData.getMwebUrl();
        Map<String, String> map = null;
        if (StringUtils.isEmpty(mwebUrl)) {
            map = MapUtil.getRespMap(appId, partnerId, prepayId, nonceStr, packageValue, timeStamp);
        } else {
            map = MapUtil.getRespMap(appId, partnerId, prepayId, nonceStr, packageValue, timeStamp, mwebUrl);
        }
        String string = MapUtil.join(map);
        String sign = Signature.getSign(string, Constants.WXPAY_KEY);
        if (sign == null) {
            log.error("sign is null.");
            return null;
        }
        PayRepData payRepData = new PayRepData(null, appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign);
        if (!StringUtils.isEmpty(mwebUrl)) {
            payRepData.setMwebUrl(mwebUrl);
        }
        return payRepData;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String alipayCallback(HttpServletRequest httpServletRequest) throws AlipayApiException {
        Map requestParams = httpServletRequest.getParameterMap();
        log.info("支付宝回调接口 -------------- 开始， requestParams = " + requestParams);
        if (requestParams == null) {
            log.error("empty requestParams.");
            return null;
        }
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Integer channel = Constants.PAY_CHANNEL_ALIPAY;
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        Boolean flag = AlipaySignature.rsaCheckV1(params, configuration.getAlipayPublicKey(), Constants.ENCODING_FORMAT_UTF_8, Constants.ALIPAY_SIGN_TYPE);
        if (!flag) {
            log.error("rsa check failed.");
            return null;
        }
        String[] tradeNoList = (String[]) requestParams.get("trade_no");
        String[] alipayLogonIdList = (String[]) requestParams.get("buyer_logon_id");
        String[] alipayUserIdList = (String[]) requestParams.get("buyer_id");
        String[] outTradeNoList = (String[]) requestParams.get("out_trade_no");
        String[] totalAmountList = (String[]) requestParams.get("total_amount");
        if (tradeNoList.length != 1) {
            log.error("invalid tradeNoList.");
            return null;
        }
        if (alipayLogonIdList.length != 1) {
            log.error("invalid zfbLogonIdList.");
            return null;
        }
        if (alipayUserIdList.length != 1) {
            log.error("invalid zfbUserIdList.");
            return null;
        }
        if (outTradeNoList.length != 1) {
            log.error("invalid outTradeNoList.");
            return null;
        }
        if (totalAmountList.length != 1) {
            log.error("invalid totalAmountList.");
            return null;
        }
        String tradeNo = tradeNoList[0];
        String alipayLogonId = alipayLogonIdList[0];
        String alipayUserId = alipayUserIdList[0];
        String outTradeNo = outTradeNoList[0];
        BigDecimal realFee = new BigDecimal(totalAmountList[0]);
        log.info("orderId: {}, outTradeNo: {}", realFee, outTradeNo);
        OrderParking orderParking = orderParkingRepository.findBySerialNumber(outTradeNo);
        //保存第三方订单号
        orderParkingRepository.updateTradeNoById(orderParking.getId(), tradeNo);
        //判断实付金额和订单状态是否合法
        if (this.checkIsPaid(orderParking, realFee)) {
            this.callBack(orderParking, channel, alipayUserId, alipayLogonId, null, realFee);
        }else{
            log.info("实付金额和订单状态不合法");
        }
        log.info("alipay callback successfully.");
        return Constants.SUCCESS;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String wxpayCallback(HttpServletRequest httpServletRequest) {
        String failXml = "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "</xml>";
        try {
            log.info("微信支付回调接口----------");
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = httpServletRequest.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
            log.info("微信支付回调接口- 相应参数 request: {}", response);
            WxpayData wxpayData = (WxpayData) Xml.fromXML(response);
            String returnCode = wxpayData.getReturnCode();
            log.info("returnCode: {}", returnCode);
            if (!returnCode.equals(Constants.WXPAY_RETURN_CODE_SUCCESS)) {
                log.error("wxpay unifiedorder failed.");
                return null;
            }
            if (!this.verifyCallback(wxpayData)) {
                log.error("回调参数校验异常 verify failed.");
                return null;
            }
            String openId = wxpayData.getOpenid();
            log.info("openId: {}", openId);
            String transactionId = wxpayData.getTransactionId();
            log.info("transactionId: {}", transactionId);
            String outTradeNo = wxpayData.getOutTradeNo();
            log.info("outTradeNo: {}", outTradeNo);
            Integer totalFee = wxpayData.getTotalFee();
            log.info("totalFee: {}", totalFee);
            Integer cashFee = wxpayData.getCashFee();
            log.info("cashFee: {}", cashFee);
            BigDecimal realFee = Money.ChinaFenToCNY(new BigDecimal(totalFee.toString()));
            log.info("outTradeNo: {}, realFee: {}", outTradeNo, realFee);
            OrderParking orderParking = orderParkingRepository.findBySerialNumber(outTradeNo);
            log.info("支付回调订单信息 orderParking = " + orderParking);
            if (orderParking != null) {
                Integer channel = orderParking.getPayChannel();
                //保存第三方订单号
                orderParkingRepository.updateTradeNoById(orderParking.getId(), transactionId);
                //判断实付金额和订单状态是否合法
                if (this.checkIsPaid(orderParking, realFee)) {
                    this.callBack(orderParking, channel, null, null, openId, realFee);
                } else {
                    return failXml;
                }
            } else {
                OrderParkingScanpay orderParkingScanpay = orderParkingScanpayRepository.findBySerialNumber(outTradeNo);
                log.info(" 微信支付回调，订单是 扫码停车支付订单的情况 , realFee = " + realFee + ", orderParkingScanpay = " + orderParkingScanpay);
                if (orderParkingScanpay == null) {
                    return failXml;
                }
                if (this.checkScanPayOrder(orderParkingScanpay, realFee)) {
                    this.callBackScanPayOrder(orderParkingScanpay, transactionId);
                } else {
                    log.info("订单校验不通过");
                    return failXml;
                }
            }
            log.info("wxpay callback successfully.");
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "</xml>";
        } catch (Exception e) {
            log.error("system error." + e);
            return null;
        }
    }

    public boolean checkScanPayOrder(OrderParkingScanpay orderParkingScanpay, BigDecimal reqRealFee) {
        if (!Status.OrderParking.USED.getInt().equals(orderParkingScanpay.getState())) {
            return false;
        }
        if (reqRealFee.compareTo(orderParkingScanpay.getRealFee()) != 0) {
            return false;
        }
        return true;
    }

    public Boolean checkIsPaid(OrderParking orderParking, BigDecimal reqRealFee) {
        if (null == orderParking) {
            return false;
        }
        //检查订单是否是待支付状态
        Integer orderState = orderParking.getState();
        Boolean isPayState = orderState.equals(Status.OrderParking.UNCONFIRMED.getInt()) || orderState.equals(Status.OrderParking.UNPAID.getInt())
                || orderState.equals(Status.OrderParking.USED.getInt());
        if (!isPayState) {
            log.error("订单状态非法, 返回失败，orderState = " + orderState );
            return false;
        }
        BigDecimal totalFee;
        Integer parkingFeePaidFlag = orderParking.getParkingFeePaidFlag();
        if(null != parkingFeePaidFlag && parkingFeePaidFlag == 1){
            totalFee = orderParking.getOvertimeFee();
        }else{
            totalFee = orderParking.getTotalFee().add(orderParking.getOvertimeFee());
        }
        BigDecimal bill = totalFee;
        //查询是否使用优惠券
        BigDecimal couponLimit = couponRepository.findCouponLimitByOrderId(orderParking.getId());
        if (null != couponLimit) {
            bill = bill.subtract(couponLimit);
        }
        bill = new BigDecimal(decimalFormat.format(bill));
        reqRealFee = new BigDecimal(decimalFormat.format(reqRealFee));
        if (bill.compareTo(reqRealFee) != 0) {
            log.error("订单金额和实际支付金额不匹配，返回失败， 订单金额bill=" + bill + ", 实际支付金额reqRealFee=" + reqRealFee);
            return false;
        }
        if (null != couponLimit) {
            //更新优惠券状态
            couponRepository.updateStateAndUseTimeAndUseMoney(orderParking.getId(), Status.Coupon.USED.getInt(), System.currentTimeMillis(), couponLimit);
        }
        return true;
    }

    public void callBackScanPayOrder(OrderParkingScanpay orderParkingScanpay, String outTradeNo) {
        Long now = System.currentTimeMillis();
        String wxPayFeeRate = configuration.getWxFeeRate();
        BigDecimal tripartiteFee = NumberUtils.getTripartiteFee(orderParkingScanpay.getRealFee(), wxPayFeeRate);
        // 更新扫码支付订单的三方流水号，三方手续费，订单状态，支付时间
        orderParkingScanpayRepository.updateInfo(orderParkingScanpay.getId(), outTradeNo, tripartiteFee, Status.OrderParking.PAID.getInt(), now);

        notifyBsg(orderParkingScanpay.getParklotId(), orderParkingScanpay.getPlateNo(), orderParkingScanpay.getRealFee(),
                orderParkingScanpay.getExtractOrderId(), orderParkingScanpay.getId().toString(), now);

    }

    /**
     * 扫码支付成功，通知bsg
     *
     * @param parklotId
     * @param plateNo
     * @param realFee
     * @param extractOrderId
     * @param orderId
     * @param payTime
     */
    public void notifyBsg(Integer parklotId, String plateNo, BigDecimal realFee, String extractOrderId, String orderId, Long payTime) {
        // 校验车辆在车场的信息
        String extraParklotId = parklotRepository.findExtraParklotIdById(parklotId);
        try {
            boolean flag = boshiBarrierService.validateParkingLotInfo(plateNo, extraParklotId);
            log.info("校验车辆在车场的信息 , flag = " + flag + ", extraParklotId = " + extraParklotId);
            if (flag) {
                String money = String.valueOf(Money.CNYToChinaFen(realFee));
                KeyTopParkingPayCostRespone keyTopParkingPayCostRespone = boshiBarrierService.payParkingLotCost(extraParklotId,
                        extractOrderId, "2", money, orderId.toString(), payTime);
                log.info("bsg支付临停费用接口相应：keyTopParkingPayCostRespone = " + keyTopParkingPayCostRespone);
            }
        } catch (Exception e) {
            log.error("订单支付成功，通知BSG接口异常，" + e);
        }
    }

    public void callBack(OrderParking orderParking, Integer channel, String alipayUserId, String alipayLogonId, String wxOpenId, BigDecimal realFee) {
        Long now = System.currentTimeMillis();
        Integer userId = orderParking.getMobileUserId();
        Integer orderState = orderParking.getState();
        Integer parklocId = orderParking.getParklocId();
        Integer parklotId = orderParking.getParklotId();
        Integer orderId = orderParking.getId();
        BigDecimal totalFee;
        Integer parkingFeePaidFlag = orderParking.getParkingFeePaidFlag();
        if(null != parkingFeePaidFlag && parkingFeePaidFlag == 1){
            totalFee = orderParking.getOvertimeFee();
        }else{
            totalFee = orderParking.getOvertimeFee().add(orderParking.getTotalFee());
        }
        BigDecimal tripartiteFee = Constants.BIGDECIMAL_ZERO;
        //更新阿里支付信息
        if (channel.equals(Constants.PAY_CHANNEL_ALIPAY)) {
            String aliPayFeeRate = configuration.getAlipayFeeRate();
            tripartiteFee = NumberUtils.getTripartiteFee(realFee, aliPayFeeRate);
            userExtraInfoRepository.updateAlipayInfo(alipayUserId, alipayLogonId, userId);
        }
        //更新微信支付信息
        else if (channel.equals(Constants.PAY_CHANNEL_WXPAY)) {
            String wxPayFeeRate = configuration.getWxFeeRate();
            tripartiteFee = NumberUtils.getTripartiteFee(realFee, wxPayFeeRate);
            userExtraInfoRepository.updateWxpayInfo(wxOpenId, userId);
        } else if (channel.equals(Constants.PAY_CHANNEL_WXPAY_PUBLIC)) {
            String wxPayFeeRate = configuration.getWxPublicFeeRate();
            tripartiteFee = NumberUtils.getTripartiteFee(realFee, wxPayFeeRate);
            userExtraInfoRepository.updateWxpayInfo(wxOpenId, userId);
        }
        //计算分成金额
        BigDecimal ownerIncome = Constants.BIGDECIMAL_ZERO;
        BigDecimal manageIncome = Constants.BIGDECIMAL_ZERO;
        BigDecimal platformIncome = Constants.BIGDECIMAL_ZERO;
        //判断业主是否是管理员
        UserMobile userMobile = userMobileRepository.findByParklocId(parklocId);
        if(null == userMobile){
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        if(Constants.PARKING_ADMIN == userMobile.getType()) {
            String managePercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
            if (StringUtils.isEmpty(managePercentage)) {
                managePercentage = Constants.PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT;
            }
            manageIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(managePercentage));
            platformIncome = NumberUtils.getPlatformFeeCeil(totalFee, ownerIncome, manageIncome);
        }else{
            String ownerPercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.OWNER_PERCENTAGE, Status.Common.VALID.getInt());
            if (StringUtils.isEmpty(ownerPercentage)) {
                ownerPercentage = Constants.OWNER_PERCENTAGE_DEFAULT;
            }
            String propcomPpercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_PERCENTAGE, Status.Common.VALID.getInt());
            if (StringUtils.isEmpty(propcomPpercentage)) {
                propcomPpercentage = Constants.PROPCOMP_PERCENTAGE_DEFAULT;
            }
            ownerIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(ownerPercentage));
            manageIncome = NumberUtils.getFeeCeil(totalFee, Integer.valueOf(propcomPpercentage));
            platformIncome = NumberUtils.getPlatformFeeCeil(totalFee, ownerIncome, manageIncome);
        }
        //更新订单状态
        if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderState)) {
            //支付预约费
            orderParkingRepository.payReserve(realFee, tripartiteFee, channel, platformIncome, ownerIncome, manageIncome, now, Status.OrderParking.RESERVED.getInt(), orderId);
            //更新订单总表
            orderTotalRepository.updateStateAndAccountBySerialNumber(orderParking.getSerialNumber(), realFee, Status.OrderParking.RESERVED.getInt());
            Parklot parklot=parklotRepository.findOne(parklotId);
            if(parklot!=null &&  Constants.PARK_LOT_CHARGE_TYPE_ZEOR.equals(parklot.getChargeType())){
                registeredTemp(orderParking);
                log.info("下发白名单成功");
            }
            if (channel.equals(Constants.PAY_CHANNEL_WXPAY_PUBLIC)) {
                if (orderParking.getMobileUserId() != null) {
                    UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(orderParking.getMobileUserId());
                    if (userExtraInfo != null && org.apache.commons.lang3.StringUtils.isNotEmpty(userExtraInfo.getWxBindOpenId())) {
                        String openId = userExtraInfo.getWxBindOpenId();
                        TemplateMsgResult templateMsgResult = wxSendTemplateService.sendPayTemplate("预约支付成功", DateUtils.timeConvertDateString(System.currentTimeMillis()), realFee, openId, orderParking.getId());
                        log.info("TemplateMsgResult {}", JsonUtils.toJsonString(templateMsgResult));
                    }
                }
            }
        } else if (Status.OrderParking.UNPAID.getInt().equals(orderState) || Status.OrderParking.USED.getInt().equals(orderState)) {
            //支付停车费
            orderParkingRepository.payParking(realFee, tripartiteFee, channel, platformIncome, ownerIncome, manageIncome, now, Status.InvoiceStatus.UNMAKE.getInt(), Status.OrderParking.PAID.getInt(), orderId);
            //更新预约订单的可开票状态
            Integer reserveOrderId = orderParking.getReserveId();
            orderParkingRepository.updateInvoiceState(reserveOrderId, Status.InvoiceStatus.UNMAKE.getInt());
            //查询停车订单对应的预约订单
            OrderParking reserveOrder = orderParkingRepository.findOne(reserveOrderId);
            //更新停车订单的可开票状态
            orderParkingRepository.updateInvoiceState(orderId, Status.InvoiceStatus.UNMAKE.getInt());
            //更新用户的可开票金额
            balanceUserRepository.updateBalanceInvoice(reserveOrder.getRealFee().add(realFee), userId);
            //计算车位所有者应有分成
            BigDecimal ownerIncomeTotal = new BigDecimal(decimalFormat.format(reserveOrder.getOwnerIncome().add(ownerIncome)));
            //更新车位所有者分成
            balanceUserRepository.updateBalanceEarnByParklocId(parklocId, ownerIncomeTotal);
            //计算车场分成
            BigDecimal manageIncomeTotal = new BigDecimal(decimalFormat.format(reserveOrder.getOwnerIncome().add(ownerIncome)));
            balanceParklotRepository.updateBalanceByParklotId(parklotId, manageIncomeTotal);
            //更新订单总表
            orderTotalRepository.updateStateAndAccountBySerialNumber(orderParking.getSerialNumber(), realFee, Status.OrderParking.PAID.getInt());
            if (channel.equals(Constants.PAY_CHANNEL_WXPAY_PUBLIC)) {
                String openId = userExtraInfoRepository.findByMobileUserId(orderParking.getMobileUserId()).getWxBindOpenId();
                TemplateMsgResult templateMsgResult = wxSendTemplateService.sendPayTemplate("停车支付成功", DateUtils.timeConvertDateString(System.currentTimeMillis()), realFee, openId, orderParking.getId());
                log.info("TemplateMsgResult {}", JsonUtils.toJsonString(templateMsgResult));
            }

            // 状态为1302，是通过扫码支付的订单
            if (Status.OrderParking.USED.getInt().equals(orderState)) {
                // 更新离场时间为当前时间
                orderMapper.updateEndTimeById(orderId);

                String plateNo = plateRepository.findByPlateId(orderParking.getPlateId(), Status.Common.VALID.getInt());
                log.info(" 原来订单状态为1302，是通过扫码支付的订单，通知bsg, plateNo = " + plateNo);
                this.notifyBsg(orderParking.getParklotId(), plateNo, realFee, orderParking.getThirdPartyNo(),
                        orderParking.getId().toString(), now);
            }
        }
    }

    public Boolean verifyCallback(WxpayData wxpayData) {
        String appId = wxpayData.getAppid();
        String bankType = wxpayData.getBankType();
        Integer cashFee = wxpayData.getCashFee();
        String feeType = wxpayData.getFeeType();
        String isSubscribe = wxpayData.getIsSubscribe();
        String mchId = wxpayData.getMchId();
        String nonceStr = wxpayData.getNonceStr();
        log.info("nonceStr: {}", nonceStr);
        String openId = wxpayData.getOpenid();
        String outTradeNo = wxpayData.getOutTradeNo();
        String resultCode = wxpayData.getResultCode();
        String returnCode = wxpayData.getReturnCode();
        String sign = wxpayData.getSign();
        if (sign == null) {
            log.error("sign is null.");
            return null;
        }
        String timeEnd = wxpayData.getTimeEnd();
        Integer totalFee = wxpayData.getTotalFee();
        String tradeType = wxpayData.getTradeType();
        String transactionId = wxpayData.getTransactionId();
        Integer couponFee = wxpayData.getCouponFee();
        Integer couponCount = wxpayData.getCouponCount();
        String couponId0 = wxpayData.getCouponId0();
        Integer couponFee0 = wxpayData.getCouponFee0();
        Map<String, String> map = MapUtil.getCallbackMap(appId, bankType, cashFee, feeType, isSubscribe, mchId, nonceStr, openId, outTradeNo, resultCode, returnCode, timeEnd, totalFee, tradeType, transactionId, couponFee, couponCount, couponId0, couponFee0);
        String string = MapUtil.join(map);
        log.info("string: {}", string);
        String newSign = Signature.getSign(string, Constants.WXPAY_KEY);
        if (newSign == null) {
            log.error("newSign is null.");
            return null;
        }
        log.info("newSign: {}, sign: {}", newSign, sign);
        if (newSign.equals(sign)) {
            return true;
        }
        return false;
    }

    @Override
    public String alipayTradeRefund(String tradeNo, BigDecimal payFee, String refundNo) {
        log.info("支付宝退款接口：tradeNo: {}, payFee: {}, refundNo : {} ", tradeNo, payFee, refundNo);
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_SERVER_URL, configuration.getAppId(), configuration.getPrivateKey(), Constants.FORMAT_JSON, Constants.ENCODING_FORMAT_UTF_8, configuration.getAlipayPublicKey(), Constants.ALIPAY_SIGN_TYPE);
        AlipayTradeRefundRequest alipayTradeRefundRequest = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
        String refundAmount = payFee.toString();
        alipayTradeRefundModel.setTradeNo(tradeNo);
        alipayTradeRefundModel.setRefundAmount(refundAmount);
        alipayTradeRefundModel.setOutRequestNo(refundNo);
        alipayTradeRefundRequest.setBizModel(alipayTradeRefundModel);
        try {
            AlipayTradeRefundResponse alipayTradeRefundResponse = alipayClient.execute(alipayTradeRefundRequest);
            String body = alipayTradeRefundResponse.getBody();
            log.info("支付宝退款接口返回数据：" + body);
            return body;
        } catch (AlipayApiException e) {
            log.error("alipayTradeRefund failed." + e);
        }
        return null;
    }


    @Override
    public String wxpayRefund(String tradeNo, BigDecimal payFee, BigDecimal fee, String refundNo, int payChannel) {
        try {
            final String channel = "payChannel";
            Map<String, String> wxResultInfo = initWxInfo(payChannel);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            log.info("wxResultInfo: {}", wxResultInfo);
            FileInputStream fileInputStream = null;
            HashMap<String, String> paramMap = new HashMap<>(16);
            if (Constants.PAY_CHANNEL_WXPAY_PUBLIC.toString().equals(wxResultInfo.get(channel).toString())) {
                fileInputStream = new FileInputStream(new java.io.File(configuration.getMpApiclientCert()));
            }
            if (Constants.PAY_CHANNEL_WXPAY.toString().equals(wxResultInfo.get(channel).toString())) {
                fileInputStream = new FileInputStream(new java.io.File(configuration.getOpApiclientCert()));
            }
            if (Constants.PAY_CHANNEL_WXPAY_XCX.toString().equals(wxResultInfo.get(channel).toString())) {
                fileInputStream = new FileInputStream(new java.io.File(configuration.getMpApiclientCert()));
            }
            keyStore.load(fileInputStream, wxResultInfo.get("mchId").toCharArray());
            SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore, wxResultInfo.get("mchId").toCharArray()).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, hostnameVerifier);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
            RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
            log.info("randomStr: {}", randomStr);


            paramMap.put("wxAppId", wxResultInfo.get("appId"));
            paramMap.put("mchId", wxResultInfo.get("mchId"));
            paramMap.put("refundAccount", Constants.WXPAY_REFUND_ACCOUNT);
            if (Constants.PAY_CHANNEL_WXPAY_PUBLIC.toString().equals(wxResultInfo.get(channel).toString())
                    || Constants.PAY_CHANNEL_WXPAY_XCX.toString().equals(wxResultInfo.get(channel).toString())) {
                log.info("refundAccount=======refundAccount",Constants.WXPAY_REFUND_ACCOUNTS);
                paramMap.put("refundAccount", Constants.WXPAY_REFUND_ACCOUNTS);
            }
            Map<String, String> map = MapUtil.getRefundMap(tradeNo, refundNo, payFee, fee, randomStr, paramMap);
            String string = MapUtil.join(map);
            String sign = Signature.getSign(string, Constants.WXPAY_KEY);
            if (sign == null) {
                log.error("sign is null.");
                return null;
            }
            Wxpay wxpay = new Wxpay();
            wxpay.setAppid(wxResultInfo.get("appId"));
            wxpay.setMchId(wxResultInfo.get("mchId"));
            wxpay.setNonceStr(randomStr);
            wxpay.setSign(sign);
            wxpay.setTransactionId(tradeNo);
            wxpay.setTotalFee(Money.CNYToChinaFen(payFee));
            wxpay.setOutRefundNo(refundNo);
            wxpay.setRefundFee(Money.CNYToChinaFen(fee));
            wxpay.setRefundAccount(paramMap.get("refundAccount"));
            String xmlStr = Xml.toXMl(wxpay);
            xmlStr = new String(xmlStr.getBytes("UTF-8"), "ISO-8859-1");
            log.info("请求微信退款接口参数xmlStr: {}", xmlStr);
            String response = restTemplate.postForObject(Constants.WXPAY_REFUND_URL, xmlStr, String.class);
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            log.info("微信退款接口响应数据， response: {}", response);
            if (Constants.PAY_CHANNEL_WXPAY_PUBLIC.toString().equals(wxResultInfo.get(channel).toString())) {
                Map<String, String> maps = Xml.xmlString2Map(response);
                if (Constants.WXPAY_RETURN_CODE_SUCCESS.equals(maps.get("result_code"))) {
                    String description = "我们已将您支付的现金或优惠券返还到您的账户，请注意查看！如有疑问请联系我们的客服";
                    OrderParking orderParking = orderParkingRepository.findByTradeNo(tradeNo);
                    if (orderParking != null && orderParking.getMobileUserId() != null) {
                        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(orderParking.getMobileUserId());
                        if (userExtraInfo != null && userExtraInfo.getWxBindOpenId() != null) {
                            TemplateMsgResult templateMsgResult = wxSendTemplateService.sendRefundTemplate("退款成功", description, DateUtils.timeConvertDateString(System.currentTimeMillis()), payFee, userExtraInfo.getWxBindOpenId(), orderParking.getId());
                            log.info("TemplateMsgResult {}", JsonUtils.toJsonString(templateMsgResult));
                        }
                    }
                }
            }
            return response;
        } catch (Exception e) {
            log.error("WxpayService wxpay refund failed." + e);
            return null;
        }
    }

    /**
     * 支付宝退款查询
     *
     * @param outTradeNo 支付订单流水号
     * @param tradeNo    支付商户订单号
     * @param refundNo   退款单号
     * @return String
     * @author: myz
     * @createTime: 2017年12月13日 下午5:54:52
     */
    @Override
    public AlipayTradeFastpayRefundQueryResponse alipayRefundQuery(String outTradeNo, String tradeNo, String refundNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_SERVER_URL, configuration.getAppId(),
                configuration.getPrivateKey(), Constants.FORMAT_JSON, Constants.ENCODING_FORMAT_UTF_8,
                configuration.getAlipayPublicKey(), Constants.ALIPAY_SIGN_TYPE);
        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel refundQueryModel = new AlipayTradeFastpayRefundQueryModel();
        // 支付宝交易号，和商户订单号不能同时为空
        refundQueryModel.setTradeNo(outTradeNo);
        // 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
        refundQueryModel.setOutTradeNo(tradeNo);
        // 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
        refundQueryModel.setOutRequestNo(refundNo);
        refundQueryRequest.setBizModel(refundQueryModel);
        try {
            /**
             * 开放平台SDK封装了同步返回验签实现，只需在创建DefaultAlipayClient对象时，设置请求网关(gateway)，应用id(app_id)，应用私钥(private_key)，
             * 编码格式(charset)，支付宝公钥(alipay_public_key)，签名类型(sign_type)即可，同步返回报文时会自动进行验签。
             */
            AlipayTradeFastpayRefundQueryResponse refundQueryResponse = alipayClient.execute(refundQueryRequest);
            log.info("支付宝查询退款接口返回数据：" + refundQueryResponse.getBody());
            return refundQueryResponse;
        } catch (AlipayApiException e) {
            log.error("调用支付宝交易退款查询接口异常：" + e);
        }
        return null;
    }

    /**
     * 微信退款查询方法
     *
     * @param outTradeNo  微信订单流水号
     * @param tradeNo     商户订单号
     * @param refundNo    商户退款单号
     * @param outRefundNo 微信退款流水号
     * @author: myz
     * @createTime: 2017年12月13日 下午7:57:07
     */
    @Override
    public String wxRefundQuery(String outTradeNo, String tradeNo, String refundNo, String outRefundNo, int payChannel) {
        String randomStr = AlphabetRandom.random(32);
        try {
            HashMap<String, String> paramMap = new HashMap<>();
            Map<String, String> wxResultMap = initWxInfo(payChannel);
            paramMap.put("wxAppId", wxResultMap.get("appId"));
            paramMap.put("mchId", wxResultMap.get("mchId"));
            Map<String, String> map = MapUtil.getRefundQueryMap(randomStr, outTradeNo, tradeNo, refundNo, outRefundNo, paramMap);
            String string = MapUtil.join(map);
            String sign = Signature.getSign(string, Constants.WXPAY_KEY);
            map.put("sign", sign);
            String xmlStr = MapUtil.mapToXML(map);
            xmlStr = new String(xmlStr.getBytes("UTF-8"), "ISO-8859-1");
            log.info("请求微信退款查询接口 xmlStr: {}", xmlStr);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(Constants.WXPAY_REFUND_QUERY_URL, xmlStr, String.class);
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            log.info("微信退款查询接口 request: {}", response);
            return response;
        } catch (Exception e) {
            log.error("请求微信退款查询接口异常：" + e);
        }
        return null;
    }

    private Map<String, String> initWxInfo(String outTradeNo) {
        OrderParking orderParking = orderParkingRepository.findByTradeNo(outTradeNo);
        return this.initWxInfo(orderParking.getPayChannel());
    }

    private Map<String, String> initWxInfo(int payChannel) {
        Map<String, String> initWxInfoMap = new HashMap<>();
        if (Constants.PAY_CHANNEL_WXPAY_PUBLIC == payChannel) {
            initWxInfoMap.put("appId", configuration.getWxMpAppId());
            initWxInfoMap.put("mchId", configuration.getMpMchId());
        } else if (Constants.PAY_CHANNEL_WXPAY == payChannel) {
            initWxInfoMap.put("appId", configuration.getWxOpAppId());
            initWxInfoMap.put("mchId", configuration.getOpMchId());
        } else if (Constants.PAY_CHANNEL_WXPAY_XCX == payChannel) {
            initWxInfoMap.put("appId",configuration.getWxXcxAppId());
            initWxInfoMap.put("mchId","1488473982");
        }
        initWxInfoMap.put("payChannel", String.valueOf(payChannel));
        return initWxInfoMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp wxScanPayParkingFee(String plateNo, Integer parklotId, String unionId, String ip, String openId) {
        Resp resp = null;

        // 查询停车费用
        KeyTopParkingCostRespone keyTopParkingCostRespone = boshiBarrierService.getPlateNumberByParkOrderCost(plateNo, System.currentTimeMillis());
        BigDecimal totalFee = Money.ChinaFenToCNY(keyTopParkingCostRespone.getData().getPkorder().getOutstandingAmount());
        String extractOrderId = keyTopParkingCostRespone.getData().getPkorder().getOrderID();
        Integer orderId = orderMapper.queryOrderIdByPlateNo(plateNo);
        // 已预约的订单
        if (orderId != null) {
            OrderParking orderParking = orderParkingRepository.findOne(orderId);
            long now = System.currentTimeMillis();
            OrderParkingData orderParkingData = this.calculateParkingOverTimeFee(now, orderParking);
            log.info("该订单是已预约订单，走以前的流程处理， orderId = " + orderId + ", totalFee = " + totalFee + ", extractOrderId = "
                    + extractOrderId + ", orderParkingData = " + orderParkingData + ", orderParking = " + orderParking);
            // 计算超时费
//            orderMapper.updateOrderById(orderId, totalFee, orderParkingData.getOvertimeFee(),
//                    extractOrderId, orderParkingData.getOvertime() > 0 ? orderParkingData.getOvertime() : null, now);

            orderParkingRepository.updateOrderById(orderId, totalFee, orderParkingData.getOvertimeFee(),
                    extractOrderId, orderParkingData.getOvertime() > 0 ? orderParkingData.getOvertime() : null, now);
            orderParking.setTotalFee(totalFee);
            orderParking.setOvertimeFee(orderParkingData.getOvertimeFee());
            orderParking.setOvertime(orderParkingData.getOvertime() > 0 ? orderParkingData.getOvertime() : null);
            orderParking.setThirdPartyNo(extractOrderId);
            orderParking.setRealEndTime(now);
            orderParkingRepository.saveAndFlush(orderParking);

            // 调用已预约停车订单支付方法
            PayRequest payRequest = new PayRequest();
            payRequest.setSpbillCreateIp(ip);
            payRequest.setChannel(Constants.PAY_CHANNEL_WXPAY_PUBLIC);
            payRequest.setOrder_id(orderId);
            payRequest.setTimestamp(String.valueOf(System.currentTimeMillis()));
            payRequest.setType(Constants.PAY_CHANNEL_WXPAY_PUBLIC);
            payRequest.setOpenId(openId);
            resp = this.pay(payRequest);

        } else {
            // 创建扫码支付订单
            String serialNumber = OrderNoGenerator.getOrderNo(Constants.RESERVATION_ORDER, "0");
            Long realStartTime = DateUtils.dateStrConvertTimestamp(keyTopParkingCostRespone.getData().getEntranceDate(), "yyyy-MM-dd'T'HH:mm:ss");
            Long realEndTime = null;
            OrderParkingScanpay orderParkingScanpay = new OrderParkingScanpay(serialNumber, unionId, parklotId, plateNo, realStartTime, realEndTime,
                    totalFee, BigDecimal.ZERO, totalFee, Constants.PAY_CHANNEL_WXPAY_PUBLIC,
                    System.currentTimeMillis(), Status.OrderParking.USED.getInt(), extractOrderId, BigDecimal.ZERO);
            orderParkingScanpayRepository.save(orderParkingScanpay);
            log.info("该订单不是已预约订单，创建扫码支付订单， totalFee = " + totalFee + ", orderParkingScanpay = " + orderParkingScanpay);
            // 调用公众号支付方法
            PayRepData payRepData = this.wxPayOffice(totalFee, null, serialNumber, openId, ip);
            resp = RespUtil.successResp(payRepData);
        }
        log.info("扫码支付  微信公众号支付 返回数据：" + resp);
        return resp;
    }

    /**
     * 计算超时费用
     * @param realEndTime 离场时间
     * @param parkingOrder
     * @return
     */
    public OrderParkingData calculateParkingOverTimeFee(Long realEndTime, OrderParking parkingOrder) {
        OrderParkingData orderParkingData = new OrderParkingData();
        BigDecimal overTimeTotalFee = BigDecimal.ZERO;
        Long overtime = 0L;
        // 停车收费规则
        ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(parkingOrder.getParklotId());
        if (infoData == null) {
            log.error("车场收费规则为空，终止计算");
            throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
        }
//        Integer index = TimeUtil.isWeekDay(System.currentTimeMillis()) ? 1 : 0;
        // 需求确认只需按照工作日的规则来算
        Integer index = 1;
        BigDecimal firstHourFee = Constants.BIGDECIMAL_ZERO;
        BigDecimal otherTimeFee = Constants.BIGDECIMAL_ZERO;
        BigDecimal maxFee = Constants.BIGDECIMAL_ZERO;
        Integer freeUseTime = Constants.MIN_NON_NEGATIVE_INTEGER;
        BigDecimal overTimeFee = Constants.BIGDECIMAL_ZERO;
        Integer dayOfWeek = 2;
        Integer type = infoData.getType();
        //如果规则分为工作日/周末
        if(1 == type){
            dayOfWeek = TimeUtil.isWeekDay(realEndTime) ? 1 : 0;
        }
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        if(null != feeRuleParkings){
            for(FeeRuleParking feeRuleParking : feeRuleParkings){
                Integer weekEnd = feeRuleParking.getWeekday();
                if(!dayOfWeek.equals(weekEnd)){
                    continue;
                }
                Long startTime = feeRuleParking.getStartTime();
                Long endTime = feeRuleParking.getEndTime();
                //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  说明其他时间段都不符合，使用该规则
                if(null == startTime){
                    overTimeFee = feeRuleParking.getOverTimeFee();
                    firstHourFee = feeRuleParking.getFirstHourFee();
                    otherTimeFee = feeRuleParking.getOtherTimeFee();
                    maxFee = feeRuleParking.getMaxFee();
                    freeUseTime = feeRuleParking.getFreeUseTime();
                    break;
                }
                //把三个时间放到同一天,跨天就加一天
                Long nowOneDay = TimeUtil.formatOneDayTime(realEndTime);
                Long startTimeOneDay = TimeUtil.formatOneDayTime(startTime);
                Long endTimeOneDay = TimeUtil.formatOneDayTime(endTime);
                if(startTimeOneDay >= endTimeOneDay){
                    endTimeOneDay = endTimeOneDay + Constants.TIMESTAMP_ONE_DAY;
                    endTime = endTime + Constants.TIMESTAMP_ONE_DAY;
                }
                //判断时间段是否符合
                if(!(nowOneDay < endTimeOneDay && nowOneDay >= startTimeOneDay)){
                    continue;
                }
                //判断时间区间是否符合
                if(!(realEndTime < endTime && realEndTime > startTime)){
                    continue;
                }
                overTimeFee = feeRuleParking.getOverTimeFee();
                break;
            }
        }
        log.info(" firstHourFee=" + firstHourFee + ", otherTimeFee=" + otherTimeFee + ", maxFee = " + maxFee
                + ", freeUseTime=" + freeUseTime + ", overTimeFee=" + overTimeFee);

        // 用户预约的时间段
        Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
        if (null != realEndTime && realEndTime > reservation.getEndTime()) {
            overtime = realEndTime - reservation.getEndTime();
        }
        // 超时情况
        if (overtime > 0) {
            // 最小计费周期
            String minChargingPeriod = parklotParamsMapper.queryParklotParamsValue(parkingOrder.getParklotId(), Constants.MIN_CHARGING_PERIOD);
            if (StringUtils.isEmpty(minChargingPeriod)) {
                minChargingPeriod = Constants.MIN_CHARGING_PERIOD_DEFAULT;
            }
            BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overtime, Integer.valueOf(minChargingPeriod)));

            // 超时费用
            overTimeTotalFee = overTimeFee.multiply(overTimePeriod);
        }
        orderParkingData.setOvertimeFee(overTimeTotalFee.setScale(2, BigDecimal.ROUND_HALF_UP));
        orderParkingData.setOvertime(overtime);
        return orderParkingData;
    }

    public PayRepData wxPayOffice(BigDecimal realFee, Integer type, String outTradeNo, String openId, String spbillCreateIp) {
        String body = Constants.PARKING_BODY;

        String response = this.officialAccountsPay(realFee, body, outTradeNo, "", spbillCreateIp, openId,5);
        WxpayData wxpayData = (WxpayData) Xml.fromXML(response);
        String returnCode = wxpayData.getReturnCode();
        if (!returnCode.equals(Constants.WXPAY_RETURN_CODE_SUCCESS)) {
            log.error("wxpay unifiedorder failed.");
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
        if (!this.verify(wxpayData)) {
            log.error("verify failed.");
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
        String timeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        String paySign = "appId=" + wxpayData.getAppid() +
                "&nonceStr=" + wxpayData.getNonceStr() +
                "&package=prepay_id=" + wxpayData.getPrepayId() +
                "&signType=MD5" +
                "&timeStamp=" + timeStamp;
        paySign = Signature.getSign(paySign, Constants.WXPAY_KEY);
        PayRepData payRepData1 = new PayRepData();
        payRepData1.setAppId(wxpayData.getAppid());
        payRepData1.setNonceStr(wxpayData.getNonceStr());
        payRepData1.setTimeStamp(timeStamp);
        payRepData1.setPrepayId(wxpayData.getPrepayId());
        payRepData1.setSign(paySign);

//        return this.getPayData(wxpayData);
        return payRepData1;
    }


    /**
     * 退款公共入口
     * @param tradeNo 第三方支付流水号
     * @param channel 支付渠道
     * @param realFee 实际支付金额(全额退款)
     * @param serialNumber 退款商户订单号
     * @return
     */
    @Override
    public String refund(String tradeNo, Integer channel, BigDecimal realFee, String serialNumber) {
        String response = "";
        log.info("申请退款 tradeNo:" + tradeNo + ", channel = " + channel + ", serialNumber = " + serialNumber);
        if (Constants.PAY_CHANNEL_ALIPAY.equals(channel)) {
            response = this.alipayTradeRefund(tradeNo, realFee, serialNumber);

        } else if (Constants.PAY_CHANNEL_WXPAY.equals(channel) || Constants.PAY_CHANNEL_WXPAY_PUBLIC.equals(channel)
                || Constants.PAY_CHANNEL_WXPAY_XCX.equals(channel)) {
            response = this.wxpayRefund(tradeNo, realFee, realFee, serialNumber, channel);
        }
//        log.info(" 退款接口响应数据： " + response);
        return response;
    }
}
