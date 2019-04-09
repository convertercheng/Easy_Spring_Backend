package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Integral;
import com.qhieco.commonentity.IntegralPermissionsLevel;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonrepo.IntegralPermissionsLevelRepository;
import com.qhieco.commonrepo.IntegralRepository;
import com.qhieco.constant.Constants;
import com.qhieco.request.web.IntegralRequest;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.IntegralService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 10:33
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class IntegralServiceImpl implements IntegralService{

    @Autowired
    private IntegralRepository integralRepository;

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Override
    public Resp query(IntegralRequest integralRequest) {
        List<Integral>  integralList= integralRepository.findAll(where(integralRequest));
        return RespUtil.successResp(integralList);
    }

    @Transactional
    @Override
    public Resp saveUpdate(List<Integral> list) throws Exception {
        for(Integral integral:list){
            integralRepository.updateIntegral(integral.getId(),integral.getIntegralPluses(),integral.getUpdateTime());
        }
        return RespUtil.successResp();
    }

    @Override
    public Resp FindIntegralOne(Integer id) {
        return null;
    }

    @Transactional
    @Override
    public Resp saveUpdateIntegral(List<IntegralRequest> list) {
        for(IntegralRequest integralRequest:list){
            if(Constants.INTEGRAL_TYPE_ONE.equals(integralRequest.getIntegralType())){
                integralRepository.updateIntegral(integralRequest.getId(),Integer.valueOf(integralRequest.getRatio().intValue()),System.currentTimeMillis());
            }
            if(Constants.INTEGRAL_TYPE_TWO.equals(integralRequest.getIntegralType())){
                integralPermissionsLevelRepository.updateIntegralPermissionsLevel(integralRequest.getId(),integralRequest.getRatio(),System.currentTimeMillis());
            }
        }
        return RespUtil.successResp();
    }

    private Specification<Integral> where(IntegralRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            val page = new PageableUtil(root, query, cb);
            page.equal("integralType", request.getIntegralType());
            return page.pridect();
        };
    }
}

