package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonentity.OrderRefund;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.commonrepo.OrderRefundRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.OrderRefundData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.webservice.OrderRefundService;
import com.qhieco.webservice.OrderService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-9 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class OrderRefundServiceImpl implements OrderRefundService {


    private QueryFunction<OrderRefund,OrderRefundRequest> queryFunction;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(orderRefundRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private OrderRefund transientProperty(OrderRefund orderRefund){
        OrderParking orderParking = orderParkingRepository.findOne(orderRefund.getOrderParkingId());
        if(orderParking != null) {
            UserMobile userMobile = userMobileRepository.findOne(orderParking.getMobileUserId());
            if (userMobile != null) {
                orderRefund.setPhone(userMobile.getPhone());
            }
        }
        String stateStr= Status.Refund.find(orderRefund.getState());
        String channelStr=Status.RefundChannel.find(orderRefund.getChannel());
        orderRefund.setStateStr(stateStr);
        orderRefund.setChannelStr(channelStr);
        return orderRefund;
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp refundQuery(OrderRefundRequest request) {
        return queryFunction.query(request,where(request));
    }

    private Specification<OrderRefund> where(OrderRefundRequest request){
        return (root, query, cb) -> {
            val page = new PageableUtil(root,query,cb);
            // 车主手机号(phone)
            if(StringUtils.isNotEmpty(request.getPhone())) {
                val userIds=userMobileRepository.findByPhoneIds("%"+request.getPhone()+"%");
                page.in("mobileUserId", userIds);
            }
            // 支付码
            if(StringUtils.isNotEmpty(request.getTradeNo())) {
                page.like("tradeNo", request.getTradeNo());
            }
            page.between("fee",request.getFeeMin(),request.getFeeMax());
            // 创建时间
            if(request.getStartCreateTime()!=null && request.getEndCreateTime()!=null) {
                page.between("createTime", request.getStartCreateTime(), request.getEndCreateTime());
            }
            if (request.getState() != null ) {
                page.equal("state", request.getState());
            }
            if(request.getChannel() !=null && request.getChannel().size()>0 ){
                page.in("channel",request.getChannel());
            }
            return page.pridect();
        };
    }

    @Override
    @EnableTenantFilter
    public Resp orderRefundExcel(OrderRefundRequest orderRefundRequest, OutputStream outputStream) throws IOException {
        return queryFunction.excel(where(orderRefundRequest),OrderRefund.class,outputStream);
    }
}
