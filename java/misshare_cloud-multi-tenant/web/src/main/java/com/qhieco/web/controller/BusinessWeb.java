package com.qhieco.web.controller;

import com.qhieco.commonentity.Business;
import com.qhieco.constant.Status;
import com.qhieco.request.web.BusinessRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.BusinessService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 13:22
 * <p>
 * 类说明：
 * 商家信息控制层
 */
@RestController
@RequestMapping("/buiness")
@Slf4j
public class BusinessWeb {

    @Autowired
    private BusinessService businessService;

    /**
     * 商家信息列表查询（分页）
     * @param businessRequest
     * @return
     */
    @PostMapping(value = "/pageable")
    public Resp all(BusinessRequest businessRequest){
        Resp checkResp = ParamCheck.check(businessRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return businessService.query(businessRequest);
    }

    /**
     * 商家详情查询
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return businessService.one(id);
    }

    /**
     * 保存商家信息
     * @param business
     * @return
     */
    @PostMapping(value = "/save")
    public Resp save(Business business){
        Resp checkResp = ParamCheck.check(business, "businessName",
                "businessStatus");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return businessService.saveUpdate(business);
    }

    /**
     * 修改商家信息
     * @param business
     * @return
     */
    @PostMapping(value = "/eidt")
    public Resp eidt(Business business){
        Resp checkResp = ParamCheck.check(business, "id",",businessName", "businessDescription",
                "businessStatus"
        );
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return businessService.saveUpdate(business);
    }

    /**
     * 删除商家信息
     * @param id
     * @return
     */
    @GetMapping(value = "/remove/{id}")
    public Resp remove(@PathVariable Integer id){
        return businessService.del(id);
    }

    /**
     * 查询有效并且是状态为正常的商家
     * @return
     */
    @GetMapping(value="/all")
    public Resp getAll(){
        return businessService.getAll();
    }

}
