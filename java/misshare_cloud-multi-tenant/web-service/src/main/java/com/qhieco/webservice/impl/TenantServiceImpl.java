package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Tenant;
import com.qhieco.commonrepo.TenantRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklocRequest;
import com.qhieco.request.web.TenantRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.TenantService;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午9:49
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class TenantServiceImpl implements TenantService{

    private QueryFunction<Tenant, TenantRequest> queryFunction;

    @Autowired
    TenantRepository tenantRepository;

    @PostConstruct
    public void init(){
        queryFunction = new QueryFunction<>(tenantRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    @Override
    public Resp<AbstractPaged<Tenant>> query(TenantRequest tenantRequest){
        return queryFunction.query(tenantRequest, where((tenantRequest)));
    }

    @Override
    public Resp save(Tenant tenant){
        if(tenant.getId()==null){
            tenant.setCreateTime(System.currentTimeMillis());
        }
        try {
            if (!PropertyCheck.uniqueCheck(tenant, "name")){
                return RespUtil.errorResp(Status.WebErr.PROPERTY_EXISTS.getCode(),
                        "租户名称已存在");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return RespUtil.successResp(tenantRepository.save(tenant));
    }

    private Specification<Tenant> where(TenantRequest request){
        return (root, query, cb) ->{
            val page = new PageableUtil(root,query,cb);
            page.like("number", request.getName());
            return page.pridect();
        };
    }

    private Tenant transientProperty(Tenant tenant){
        return tenant;
    }
}
