package com.qhieco.webservice.impl;

import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonrepo.iot.BarrierRepository;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.BarrierRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.BarrierData;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.BarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 下午3:30
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class BarrierServiceImpl extends AbstractIotServiceImpl<Barrier, BarrierRequest> implements BarrierService{

    @Autowired
    BarrierRepository barrierRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Override
    public IotRepository getDao() {
        return barrierRepository;
    }

    @Override
    protected Specification<Barrier> unbindWhere(BarrierRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            // 未绑定小区
            PageableUtil.isNull(root, cb, predicates, "parklotId");
            // 服务器ip(serverIp)
            PageableUtil.like(root, cb, predicates, "serverIp", request.getServerIp());
            // 端口号(serverPort)
            PageableUtil.equal(root, cb, predicates, "serverPort", request.getServerPort());
            // 道闸名称
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Barrier data) {
        if (data.getParklotId() != null) {
            Parklot parklot = parklotRepository.findOne(data.getParklotId());
            if (parklot != null) {
                data.setParklotName(parklot.getName());
                data.setParklotTypeStr(Status.ParklotType.find(parklot.getType()));
                data.setParklotType(parklot.getType());
                data.setInnershare(parklot.getInnershare());
                data.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
            }
        }
    }

    @Override
    protected void reverseLookup(Map<String, Object> map) {

    }


    @Override
    protected Specification<Barrier> where(BarrierRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            // 小区名称(estateName)
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

    @Override
    public Resp<List<BarrierData>> estateBarrier(Integer estateId) {
        List<Barrier> barrierList = barrierRepository.findByparklotId(estateId,Status.Common.VALID.getInt());
        if (barrierList == null || barrierList.size() == 0) {
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),Status.WebErr.ENTITY_NOT_EXISTS.getMsg());
        }
        List<BarrierData> barrierDataList= new ArrayList<>();
        for (int i = 0; i< barrierList.size(); i++){
            Barrier barrier = barrierList.get(i);
            BarrierData barrierData = new BarrierData();
            if (barrier.getParklotId() != null) {
                Parklot parklot = parklotRepository.findOne(barrier.getParklotId());
                if (parklot != null) {
                    barrierData.setParklotName(parklot.getName());
                }
            }
            BeanUtil.converJavaBean(barrier, barrierData);
            barrierDataList.add(barrierData);
        }
        return RespUtil.successResp(barrierDataList);
    }
}
