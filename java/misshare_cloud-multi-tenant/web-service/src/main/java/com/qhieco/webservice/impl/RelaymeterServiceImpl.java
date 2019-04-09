package com.qhieco.webservice.impl;

import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.iotdevice.Relaymeter;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.commonrepo.iot.RelaymeterRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RelaymeterRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.RelaymeterData;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.RelaymeterService;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-20 上午11:12
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class RelaymeterServiceImpl extends AbstractIotServiceImpl<Relaymeter, RelaymeterRequest> implements RelaymeterService{

    private static final String STATE = "state";
    private static final String TYPE = "type";
    @Autowired
    RelaymeterRepository relaymeterRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Override
    public IotRepository getDao() {
        return relaymeterRepository;
    }

    @Override
    protected Specification<Relaymeter> where(RelaymeterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            PageableUtil.like(root, cb, predicates, "number", request.getNumber());
            if(StringUtils.isNotEmpty(request.getManufacturerName())){
                PageableUtil.like(root, cb, predicates, "manufacturerName", request.getManufacturerName());
            }
            if(StringUtils.isNotEmpty(request.getModel())){
                PageableUtil.like(root, cb, predicates, "model", request.getModel());
            }
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
    protected Specification<Relaymeter> unbindWhere(RelaymeterRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            PageableUtil.isNull(root, cb, predicates, "parklotId");
            PageableUtil.like(root, cb, predicates, "number", request.getNumber());
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Relaymeter data) {
        if (data.getParklotId() != null) {
            Parklot parklot = parklotRepository.findOne(data.getParklotId());
            if (parklot != null) {
                data.setParklotName(parklot.getName());
            }
        }
    }

    @Override
    protected void reverseLookup(Map map) {
        if (map.get(STATE) != null){
            map.put("stateStr", Status.Common.find((Integer) map.get("state")));
        }
        if (map.get(TYPE) != null) {
            map.put("typeStr", Status.RelayType.find((Integer) map.get("type")));
        }
    }

    @Override
    @EnableTenantFilter
    public Resp query(RelaymeterRequest request) {
        val resp = super.query(request);
        if (!resp.getError_code().equals(Status.WebErr.SUCCESS.getCode())){
            return resp;
        }
        val data = resp.getData();
        val dataList = data.getDataList();
        Integer invalidNum = 0;
        Long now = System.currentTimeMillis();
        for (Relaymeter relaymeter: dataList) {
            Long updateTime = relaymeter.getUpdateTime();
            if(null != updateTime){
                if (now - updateTime >= 300000){
                    invalidNum ++;
                }
            }else{
                invalidNum ++;
            }

        }
        RelaymeterData relaymeterData = new RelaymeterData();
        BeanUtil.converJavaBean(resp.getData(),relaymeterData);
        relaymeterData.setInvalidNum(invalidNum);
        return RespUtil.successResp(relaymeterData);
    }

    @Override
    public Resp save(Relaymeter data) {
        data.setUpdateTime(System.currentTimeMillis());
        data.setState(Status.Common.VALID.getInt());
        setHasRelay(data);
        relaymeterRepository.save(data);
        return RespUtil.successResp();
    }

    /**
     * 设置停车区有计数器的标记
     * @param data relaymeter
     */
    private void setHasRelay(Relaymeter data) {
        Integer parklotId = data.getParklotId();
        Parklot parklot = parklotRepository.findOne(parklotId);
        parklot.setHasRelay(Constants.HasRelayMeter.YES.ordinal());
        if (null == parklotRepository.save(parklot)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
    }

    @Override
    public Relaymeter findRelaymeterByName(String name, Integer state) {
        return relaymeterRepository.findRelaymeterByName(name,state);
    }

    @Override
    public Relaymeter findRelaymeterByName(String name, Integer state, Integer id) {
        return relaymeterRepository.findRelaymeterByName(name,state,id);
    }
}
