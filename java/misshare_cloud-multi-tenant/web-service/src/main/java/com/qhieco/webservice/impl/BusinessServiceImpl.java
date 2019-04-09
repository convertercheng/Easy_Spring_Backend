package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Business;
import com.qhieco.commonrepo.BusinessRepository;
import com.qhieco.commonrepo.CouponRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.BusinessRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.BusinessService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 10:52
 * <p>
 * 类说明：
 * 商家信息业务层
 */
@Service
public class BusinessServiceImpl implements BusinessService{

    @Autowired
    private BusinessRepository businessRepository;


    private QueryFunction<Business,BusinessRequest> queryFunction;

    @Autowired
    private CouponRepository couponRepository;

    @PostConstruct
    public void init(){
        queryFunction = new QueryFunction<>(businessRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private Business transientProperty(Business data){
        val newData = new Business();
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }

    @Override
    public Resp<AbstractPaged<Business>> query(BusinessRequest businessRequest) {
        businessRequest.setState(Status.Common.VALID.getInt());
        return  queryFunction.query(businessRequest,where(businessRequest));
    }

    @Override
    public Resp getAll() {
            BusinessRequest businessRequest=new BusinessRequest();
            businessRequest.setState(Status.Common.VALID.getInt());
            businessRequest.setBusinessStatus(Status.Business.BUSINESS_STATUS_NORMAL.getInt());
        return  RespUtil.successResp(businessRepository.findAll(allWhere(businessRequest)));
    }
    private Specification<Business> allWhere(BusinessRequest request){
        return (root, query, cb) ->{
            val page = new PageableUtil(root,query,cb);
            page.equal("state",request.getState());
            page.equal("businessStatus",request.getBusinessStatus());
            return page.pridect();
        };
    }


    @Transactional
    @Override
    public Resp saveUpdate(Business business) {
        Business bus=null;
        if(business.getId()!=null){
            Business businessName=businessRepository.findBusinessByName(business.getBusinessName()
                    ,business.getId(),Status.Common.VALID.getInt());
            if(businessName!=null){
                return RespUtil.errorResp(Status.WebErr.BUSINESS_EXISTS.getCode(),
                        Status.WebErr.BUSINESS_EXISTS.getMsg());
            }
            Business business1=businessRepository.findOne(business.getId());
            business.setModifyTime(System.currentTimeMillis());
            business.setCreateTime(business1.getCreateTime());
            business.setState(business1.getState());
            business.setBusinessStatus(business.getBusinessStatus());
            if(business.getBusinessStatus()==Status.Business.BUSINESS_STATUS_END.getInt()){
                couponRepository.delCoupon(business1.getId(),Status.Coupon.UNCLAIMED.getInt());
            }
        }else{
            Business business1=businessRepository.findBusinessByName(business.getBusinessName()
            ,Status.Common.VALID.getInt());
            if(business1!=null){
                return RespUtil.errorResp(Status.WebErr.BUSINESS_EXISTS.getCode(),
                        Status.WebErr.BUSINESS_EXISTS.getMsg());
            }
            business.setState(Status.Common.VALID.getInt());
            business.setCreateTime(System.currentTimeMillis());
        }
        bus=businessRepository.save(business);
        if(bus!=null){
            return RespUtil.successResp();
        }
        return RespUtil.errorResp(Status.WebErr.INSERT_ERROR.getCode(),
                Status.WebErr.INSERT_ERROR.getMsg());
    }

    @Override
    public Resp one(Integer id) {
        Business bus=businessRepository.findOne(id);
        if(bus==null){
            RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(),
                    Status.WebErr.SYSTEM_ERROR.getMsg());
        }
        return RespUtil.successResp(bus);
    }

    @Transactional
    @Override
    public Resp del(Integer id) {
        businessRepository.deleteBusiness(id,Status.Common.DELETED.getInt(),
                Status.Business.BUSINESS_STATUS_END.getInt());
        couponRepository.delCoupon(id,Status.Coupon.UNCLAIMED.getInt());
        return RespUtil.successResp();
    }


    private Specification<Business> where(BusinessRequest request){
        return (root, query, cb) ->{
            val page = new PageableUtil(root,query,cb);
            if(StringUtils.isNotEmpty(request.getBusinessName())) {
                page.like("businessName", request.getBusinessName());
            }
            if(request.getBusinessStatus()!=null) {
                page.equal("businessStatus",request.getBusinessStatus());
            }
            page.equal("state",request.getState());
            return page.pridect();
        };
    }
}
