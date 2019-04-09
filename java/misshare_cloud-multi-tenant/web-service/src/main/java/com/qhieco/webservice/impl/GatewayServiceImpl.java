package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Area;
import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.GatewayRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.commonrepo.iot.RouterRepository;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AreaService;
import com.qhieco.webservice.GatewayService;
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
 * @version 2.0.1 创建时间: 18-3-21 下午4:29
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class GatewayServiceImpl extends AbstractIotServiceImpl<Gateway, GatewayRequest> implements GatewayService{

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    RouterRepository routerRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    AreaService areaService;

    public GatewayServiceImpl(){
        condition = "identifier";
    }

    @Override
    public IotRepository<Gateway> getDao() {
        return gatewayRepository;
    }

    @Override
    protected Specification<Gateway> where(GatewayRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (request.getParklotName() != null && !"".equals(request.getParklotName())) {
                List<Integer> ids = gatewayRepository.findIdbyParklotName("%" + request.getParklotName() + "%");
                if (ids.size() > 0) {
                    predicates.add(root.<Integer>get("id").in(ids));
                }else {
                    predicates.add(root.<Integer>get("id").in(-1));
                }
            }
//            if (request.getRouterName() != null && !request.getRouterName().equals("")) {
//                List<Integer> ids = gatewayRepository.findIdbyRouterName("%" + request.getRouterName() + "%");
//                if (ids.size() > 0) {
//                    predicates.add(root.<Integer>get("id").in(ids));
//                }else {
//                    predicates.add(root.<Integer>get("id").in(-1));
//                }
//            }
//            if (request.getRouterNumber() != null && !request.getRouterNumber().equals("")) {
//                List<Integer> ids = gatewayRepository.findIdbyRouterNumber("%" + request.getRouterNumber() + "%");
//                if (ids.size() > 0) {
//                    predicates.add(root.<Integer>get("id").in(ids));
//                }else {
//                    predicates.add(root.<Integer>get("id").in(-1));
//                }
//            }

//            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.equal(root, cb, predicates, "routerId", request.getRouterId());
            PageableUtil.like(root, cb, predicates, "identifier", request.getIdentifier());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected Specification<Gateway> unbindWhere(GatewayRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            List<Integer> ids=new ArrayList<>();
            if("1".equals(request.getType())){
                PageableUtil.isNull(root, cb, predicates, "routerId");
            }else{
                ids=lockRepository.findAllByGatewayId();
                if(ids.size()>0){
                    predicates.add(root.<Integer>get("id").in(ids).not());
                }
            }
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.like(root, cb, predicates, "identifier", request.getIdentifier());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Gateway data) {
        data.setLockNum(lockRepository.countByGatwayId(data.getId()));
        if(data.getRouterId() == null) {
            return;
        }
        val router = routerRepository.findOne(data.getRouterId());
        if (router == null || router.getParklotId() == null) {
            return;
        }
        data.setRouterName(data.getName());
        val parklot = parklotRepository.findOne(router.getParklotId());
        if (parklot !=null){
            data.setParklotName(parklot.getName());
            data.setAreaList(areaService.findAreaList(parklot.getAreaId()));
        }
    }

    @Override
    protected void reverseLookup(Map<String, Object> map) {
        val areas =  (List<Area>)  map.get("areaList");
        for (Area area:areas
             ) {
            switch (area.getLevel()){
                case 1:
                    map.put("province", area.getName());
                    break;
                case 2:
                    map.put("city", area.getName());
                    break;
                case 3:
                    map.put("district", area.getName());
                    break;
                default:
            }
        }
    }

    @Override
    public Resp<Gateway> one(Integer id) {
        Gateway gateway=gatewayRepository.findOne(id);
        if(gateway.getRouterId()!=null){
            gateway.setRouterName(routerRepository.findOne(gateway.getRouterId()).getName());
        }
        return RespUtil.successResp(gateway);
    }
}
