package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.AccessRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AccessRequest;
import com.qhieco.util.PageableUtil;
import com.qhieco.webservice.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午5:46
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class AccessServiceImpl extends AbstractIotServiceImpl<Access, AccessRequest> implements AccessService{
    @Autowired
    AccessRepository accessRepository;

    @Autowired
    ParklotRepository parklotRepository;

    public AccessServiceImpl(){
        condition = "parklotId&name|parklotId&btName";
    }

    @Override
    public IotRepository<Access> getDao(){
        return accessRepository;
    }

    @Override
    protected Specification<Access> unbindWhere(AccessRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            PageableUtil.isNull(root, cb, predicates, "parklotId");
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.equal(root, cb, predicates, "id", request.getId());
            PageableUtil.like(root, cb, predicates, "btName", request.getBtName());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Access access){
        if (access.getParklotId() != null) {
            Parklot parklot = parklotRepository.findOne(access.getParklotId());
            if (parklot != null) {
                access.setParklotName(parklot.getName());
                access.setParklotTypeStr(Status.ParklotType.find(parklot.getType()));
                access.setParklotType(parklot.getType());
                access.setInnershare(parklot.getInnershare());
                access.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
            }
        }
    }

    @Override
    protected void reverseLookup(Map<String, Object> map) {
        map.put("stateStr", Status.Common.find((Integer) map.get("state")));
    }

    @Override
    protected Specification<Access> where(AccessRequest request){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            if (request.getParklotName() != null && !"".equals(request.getParklotName())) {
                List<Integer> parklotIds = parklotRepository.findIdsByNameLike("%" + request.getParklotName() + "%");
                if (parklotIds.size() > 0) {
                    predicates.add(root.<Integer>get("parklotId").in(parklotIds));
                }else {
                    predicates.add(root.<Integer>get("parklotId").in(-1));
                }
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
