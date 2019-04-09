package com.qhieco.webservice.impl;

import com.qhieco.commonentity.iotdevice.Router;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.GatewayRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.commonrepo.iot.RouterRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.request.web.RouterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.PageableUtil;
import com.qhieco.webservice.GatewayService;
import com.qhieco.webservice.RouterService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-22 下午3:08
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class RouterServiceImpl extends AbstractIotServiceImpl<Router, RouterRequest> implements RouterService{
    @Autowired
    RouterRepository routerRepository;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    public RouterServiceImpl(){
        condition = "name&number&model&manufacturerName";
    }

    @Override
    public IotRepository<Router> getDao() {
        return routerRepository;
    }

    @Override
    protected Specification<Router> where(RouterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (request.getParklotName() != null && !"".equals(request.getParklotName())) {
                List<Integer> ids = routerRepository.findIdbyParklotName("%" + request.getParklotName() + "%");
                if (ids.size() > 0) {
                    predicates.add(root.<Integer>get("id").in(ids));
                }else {
                    predicates.add(root.<Integer>get("id").in(-1));
                }
            }
            PageableUtil.like(root,cb,predicates,"manufacturerName",request.getManufacturerName());
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.like(root, cb, predicates, "model", request.getModel());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected Specification<Router> unbindWhere(RouterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            PageableUtil.isNull(root, cb, predicates, "parklotId");
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.like(root, cb, predicates, "number", request.getNumber());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Router data) {
        if(data.getParklotId() != null) {
            val parklot = parklotRepository.findOne(data.getParklotId());
            if(parklot!=null){
                data.setParklotName(parklot.getName());
            }
        }
    }

    @Override
    protected void reverseLookup(Map<String, Object> map) {
        Integer parklotId = (Integer) map.get("parklotId");
        if(parklotId!=null){
            val parklot = parklotRepository.findOne(parklotId);
            if(parklot!=null){
                map.put("parklotName", parklot.getName());
            }
        }
    }

    @Override
    public Resp one(GatewayRequest request){
        val resp = super.one(request.getRouterId());
        if(! resp.getError_code().equals(Status.WebErr.SUCCESS.getCode())){
            return resp;
        }
        Router router = resp.getData();
        val gateways = gatewayService.query(request);
        val dataList = gateways.getData();
        dataList.getDataList().forEach(gateway -> gateway.setAreaList(null));
        if (dataList!=null){
            router.setGatewayList(dataList);
        }
        return resp;
    }
}
