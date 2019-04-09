package com.qhieco.lock.web;

import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.constant.Status;
import com.qhieco.lock.exception.QhieException;
import com.qhieco.lock.service.LockService;
import com.qhieco.lock.service.NBLOTService;
import com.qhieco.lock.service.impl.NBIOTLockServiceImpl;
import com.qhieco.request.api.*;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 14:29
 * <p>
 * 类说明：
 * 车锁接口层
 */

@RestController
@RequestMapping
public class LockWeb {


    @Autowired
    private LockService lockService;

    @Autowired
    private RestTemplate ribbonTemplate;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    NBLOTService nblotService;

    @PostMapping("/control")
    public Resp control(@RequestBody LockControlRequest lockControlRequest) {
        Resp resp = ParamCheck.check(lockControlRequest,  "lock_id","command");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        Lock lockIot = lockRepository.findOne(lockControlRequest.getLock_id());
        if (null == lockIot) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_LOCK);
        }
        if(Status.Lock.LOCKZIGB.getInt().equals(lockIot.getType())){
            return lockService.control(lockControlRequest);
        }
        if(Status.Lock.LOCKNB.getInt().equals(lockIot.getType())){
            return nblotService.control(lockControlRequest);
        }
        return RespUtil.errorResp(Status.ApiErr.NONEXISTENT_LOCK.getCode(), "暂不支持该类型车位锁");
    }

    @PostMapping("batch/control")
    public Resp batchControl(@RequestBody LockBatchControlRequest request) {
        Resp resp = ParamCheck.check(request,  "lock_ids","command");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        String[] lockStrs = request.getLock_ids().split(",");
        Integer command = request.getCommand();
        List<Integer> lockIdList = new ArrayList<>();
        for(String lockIdStr : lockStrs){
            lockIdList.add(Integer.valueOf(lockIdStr));
        }
        List<Lock> locks = lockRepository.findAll(lockIdList);
        List<Integer> zigbeeLock = new ArrayList<>();
        List<Integer> nbLock = new ArrayList<>();
        locks.forEach(lock -> {
            if(Status.Lock.LOCKZIGB.getInt().equals(lock.getType())){
                zigbeeLock.add(lock.getId());
            }
            if(Status.Lock.LOCKNB.getInt().equals(lock.getType())){
                nbLock.add(lock.getId());
            }
        });
        if(zigbeeLock.size()>0){
            lockService.batchControl(new LockBatchControlRequest(command, zigbeeLock, request.getTimestamp()));
        }
        if(nbLock.size()>0){
            nblotService.batchControl(new LockBatchControlRequest(command, nbLock, request.getTimestamp()));
        }
        return RespUtil.successResp(System.currentTimeMillis());
    }

    @PostMapping("/state")
    public Resp getState(@RequestBody LockGetStateRequest lockGetStateRequest) {
        Resp resp = ParamCheck.check(lockGetStateRequest,  "lock_id","command","control_time");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return lockService.getState(lockGetStateRequest);
    }

    @PostMapping("/waitTime")
    public Resp waitTime(@RequestBody OrderIdRequest orderIdRequest) {
        Resp resp = ParamCheck.check(orderIdRequest,  "order_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return lockService.waitTime(orderIdRequest);
    }

    @GetMapping("/testRibbon")
    public String ribbon(){
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("parkLotId", 1);
        jsonParams.put("parkLoctId", 1);
        jsonParams.put("tag", 1);
        String result = ribbonTemplate.postForEntity("http://apiwrite/reserve/process",jsonParams,String.class).getBody();
//                        String result = HttpUtil.doPosts(url, jsonParams);
        System.out.println("调用修改停车订单接口 响应结果：" + result);
        return result;
    }
}
