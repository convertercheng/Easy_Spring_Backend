package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Params;
import com.qhieco.commonrepo.ParamsRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParamsRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.ParamsService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 17:10
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class ParamsServiceImpl implements ParamsService {

    @Autowired
    private ParamsRepository paramsRepository;

    private QueryFunction<Params, ParamsRequest> queryFunction;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(paramsRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private Params transientProperty(Params data) {
        val newData = new Params();
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }

    @Override
    public Resp<AbstractPaged<Params>> query(ParamsRequest paramsRequest) {
        return queryFunction.query(paramsRequest, where(paramsRequest));
    }

    @Override
    public Resp saveUpdate(ParamsRequest paramsRequest) {
        Params params = null;
        if (paramsRequest.getId() != null) {
            Params paramsBykey = paramsRepository.findParamsBykey(paramsRequest.getQhkey(),
                    Status.Common.VALID.getInt(), paramsRequest.getId());
            if (paramsBykey != null) {
                return RespUtil.errorResp(Status.WebErr.BUSINESS_EXISTS.getCode(),
                        Status.WebErr.BUSINESS_EXISTS.getMsg());
            }
            params = paramsRepository.findOne(paramsRequest.getId());
            if (paramsRequest.getState() != null) {
                params.setState(paramsRequest.getState());
            }
            if (StringUtils.isNotEmpty(paramsRequest.getQhkey())) {
                params.setQhKey(paramsRequest.getQhkey());
            }
            if (StringUtils.isNotEmpty(paramsRequest.getQhvalue())) {
                params.setQhValue(paramsRequest.getQhvalue());
            }
            if (paramsRequest.getLevel() != null) {
                params.setLevel(paramsRequest.getLevel());
            }
        } else {
            Params paramsBykey = paramsRepository.findParamsBykey(paramsRequest.getQhkey(),
                    Status.Common.VALID.getInt());
            if (paramsBykey != null) {
                return RespUtil.errorResp(Status.WebErr.BUSINESS_EXISTS.getCode(),
                        Status.WebErr.BUSINESS_EXISTS.getMsg());
            }
            params=new Params();
            if (paramsRequest.getState() == null) {
                paramsRequest.setState(Status.Common.VALID.getInt());
            }
            params.setState(paramsRequest.getState());
            if (StringUtils.isNotEmpty(paramsRequest.getQhkey())) {
                params.setQhKey(paramsRequest.getQhkey());
            }
            if (StringUtils.isNotEmpty(paramsRequest.getQhvalue())) {
                params.setQhValue(paramsRequest.getQhvalue());
            }
            if (paramsRequest.getLevel() != null) {
                params.setLevel(paramsRequest.getLevel());
            }
        }
        paramsRepository.save(params);
        return RespUtil.successResp();
    }

    @Override
    public Resp one(Integer id) {
        return RespUtil.successResp(paramsRepository.findOne(id));
    }

    private Specification<Params> where(ParamsRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            return page.pridect();
        };
    }
}
