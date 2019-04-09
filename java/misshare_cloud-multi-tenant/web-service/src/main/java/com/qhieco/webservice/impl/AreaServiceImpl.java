package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Area;
import com.qhieco.commonrepo.AreaRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AreaRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.AreaData;
import com.qhieco.util.EliminateNull;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AreaService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:01
 *          <p>
 *          类说明：
 *          AreaService的实现类
 */
@Service
@Slf4j
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Override
    public Resp save(Area area) {
        long time = System.currentTimeMillis();
        Integer id = area.getId();
        Area dest = area;
        if(id != null) {
            try {
                dest = EliminateNull.eliminate(area, areaRepository.findOne(id));
            } catch (NoSuchFieldException | SecurityException | InvocationTargetException | IllegalAccessException
                    | IllegalArgumentException | IntrospectionException e) {
                return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
            }
        }
        if (dest == null) {
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
        Area sameArea = areaRepository.findByNameAndLevel(dest.getName(), dest.getLevel());
        if (sameArea != null) {
            if (id == null || !id.equals(sameArea.getId())) {
                return RespUtil.errorResp(Status.WebErr.DISTRICT_EXISTS.getCode(), Status.WebErr.DISTRICT_EXISTS.getMsg());
            }
        }
        if (dest.getState() == null) {
            dest.setState(Status.Common.VALID.getInt());
        }
        if (id == null) {
            dest.setCreateTime(time);
        }
        dest.setModifyTime(time);
        return RespUtil.successResp(areaRepository.save(dest));
    }

    @Override
    public Resp<AbstractPaged<Area>> children(AreaRequest areaRequest) {
        Page<Area> page = areaRepository.findAll(allWhere(areaRequest.getParentId(), Status.Common.VALID.getInt()),
                PageableUtil.pageable(areaRequest.getSEcho(), areaRequest.getStart(), areaRequest.getLength()));
        List<Area> areaList = page.getContent();
        Integer count = (int) page.getTotalElements();
        AbstractPaged<Area> data = AbstractPaged.<Area>builder().sEcho(areaRequest.getSEcho() + 1).iTotalRecords(count)
                .iTotalDisplayRecords(count).dataList(areaList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public List<AreaData> getAreas(Integer parentId) {
        List<Area> areas =  areaRepository.findByParentIdAndState(parentId, Status.Common.VALID.getInt());
        List<AreaData> areaDataList = new ArrayList<>();
        AreaData areaData;
        for (Area area: areas) {
            areaData = new AreaData(area.getId(), area.getName());
            areaDataList.add(areaData);
        }
        return areaDataList;
    }

    private Specification<Area> allWhere(final Integer parentId, final Integer state) {
        return new Specification<Area>() {

            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                // 父级地域ID(parentId)
                PageableUtil.equal(root, cb, predicates, "parentId", parentId);
                // 状态(state)
                PageableUtil.equal(root, cb, predicates, "state", state);
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }

        };
    }

    @Override
    public List<Area> findAreaList(Integer areaId){
        val area = areaRepository.findOne(areaId);
        if(area == null ){
            return new ArrayList<Area>();
        }
        val areaList = findAreaList(area.getParentId());
        areaList.add(area);
        return areaList;
    }
}
