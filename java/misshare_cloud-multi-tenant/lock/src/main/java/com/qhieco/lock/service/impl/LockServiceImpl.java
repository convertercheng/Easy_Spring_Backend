package com.qhieco.lock.service.impl;

import com.google.gson.Gson;
import com.qhieco.commonentity.LogLock;
import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.LogLockRepository;
import com.qhieco.commonrepo.ParklocRepository;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.iot.GatewayRepository;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.lock.exception.QhieException;
import com.qhieco.lock.service.LockService;
import com.qhieco.request.api.LockBatchControlRequest;
import com.qhieco.request.api.LockControlRequest;
import com.qhieco.request.api.LockGetStateRequest;
import com.qhieco.request.api.OrderIdRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.DeviceData;
import com.qhieco.response.data.api.LockData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.HttpUtil;
import com.qhieco.util.LockUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Condition;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/28 上午11:04
 * <p>
 * 类说明：
 *     车位锁具体业务层
 */

@Service
@Slf4j
public class LockServiceImpl implements LockService {

    Gson gson = new Gson();
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    @Autowired
    ConfigurationFiles configurationFiles;

    @Autowired
    LogLockRepository logLockRepository;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    private RestTemplate ribbonTemplate;

    @Autowired
    private RedisService redisService;

    private String gatewayhost;

    private static Boolean flag  = false;



    public LockServiceImpl( @Value("${GATEWAY_HOST}") String gatewayhost) {
        log.info("LockServiceImpl init");
        this.gatewayhost = gatewayhost;
        connect();
        subscribe();
        if (logLockRepository == null || lockRepository == null || parklocRepository==null || parklotRepository==null) {
            log.error("parkingDao or parkingService or lockRepository is null or parklocRepository is null or parklotRepository is null");
        }
    }

    public void connect() {
        try {
//            String gatewayhost = "";
            Properties prop = new Properties();
            FileInputStream applicationIn = null;
            FileInputStream activeIn = null;
            try {
//                String path = LockService.class.getClassLoader().getResource("application.yml").getPath();
//                log.info("启动服务读取application.yml：" + path);
//                applicationIn = new FileInputStream(path);
//                prop.load(applicationIn);
//                String active = prop.getProperty("active");
//                log.info("启动服务链接车位锁网关，active =" + active);
//
//                path = LockService.class.getClassLoader().getResource("application-" + active + ".yml").getPath();
//                log.info("启动服务读取application-" + active + ".properties文件路径：" + path);
//                activeIn = new FileInputStream(path);
//                prop.load(activeIn);
//                gatewayhost = prop.getProperty("GATEWAY_HOST");
                log.info("启动服务链接车位锁网关，gatewayhost = " + gatewayhost);
            } catch (Exception e) {
                log.error("启动服务连接车位锁网关异常， " + e);
            } finally {
                if (activeIn != null) {
                    try {
                        activeIn.close();
                    } catch (IOException e) {
                        log.error("关闭io异常..");
                    }
                }
                if (applicationIn != null) {
                    try {
                        applicationIn.close();
                    } catch (IOException e) {
                        log.error("关闭io异常...");
                    }
                }
            }
            String clientId = MqttClient.generateClientId();
            mqttClient = new MqttClient(gatewayhost, clientId, new MemoryPersistence());
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            GateWayCallback gateWayCallback = new GateWayCallback();
            mqttClient.setCallback(gateWayCallback);
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setKeepAliveInterval(20);
//            mqttConnectOptions.setAutomaticReconnect(true);
            mqttClient.connect(mqttConnectOptions);
            log.info("MQTT连接结果：" + mqttClient.isConnected() +", clientId=" + clientId);
        } catch (MqttException e) {
            log.error("connect failed." + e);
        }
    }

    private class GateWayCallback implements MqttCallbackExtended {
        @Override
        public void connectionLost(Throwable throwable) {
            log.error("the connection was lost." + mqttClient.isConnected());
//            if (mqttClient == null || !mqttClient.isConnected()) {
//                if (flag) {
//                    return;
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        flag = true;
//                        loop();
//                        flag = false;
//                    }
//                }).start();
//            }
        }

        public void loop() {
            while (true) {
                try {
                    if (mqttClient == null || !mqttClient.isConnected()) {
                        log.info("失去连接重连MQTT");
                        log.info("线程id:"+Thread.currentThread().getId());
                        connect();
                        subscribe();
                    }else {
                        if (mqttClient.isConnected()){
                            break;
                        }
                    }
                    Thread.sleep(60000);
                } catch (Exception e) {

                }
            }
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            String json = new String(mqttMessage.getPayload());
            log.info("车位锁调用 messageArrived方法 。。。。json=" + json + ", s = " + s + ", lockRepository = " + lockRepository
                    + ", logLockRepository=" + logLockRepository);
            log.info("topic: {}, qos: {}, payload: {}", s, mqttMessage.getQos(), json);
            LockData lockData = gson.fromJson(json, LockData.class);
            String gatewayId = lockData.getGatewayId();
            DeviceData deviceData = lockData.getDevice();
            String type = deviceData.getType();
            /*
             * 回复数据类型， 分为 DATA、ACK 和 HEARTBEAT。
             * DATA 表示该帧为数据帧，需要解析 payload 数据；
             * ACK 表示应答帧， 是针对“CON”型指令的应答消息；
             * HEARTBEAT为心跳指令， 当持续 100 秒没有数据通信时网关主动发送一次心跳数据， 心跳数据使用网关专用的虚拟地址。
             */
            // 车位锁的 mac 地址
            String mac = deviceData.getMac();
            String payload = deviceData.getPayload();
            log.info("gatewayId: {}, mac: {}, payload: {}", gatewayId, mac, payload);
            // 非DATA类型的直接终止
            if (!Constants.LOCK_DATA.equals(type)) {
                return;
            }
            // 剩余电量信息
            String batteryHex = payload.substring(Constants.LOCK_BATTERYHEX_INDEX_START, Constants.LOCK_BATTERYHEX_INDEX_END);
            Double battery = LockUtil.getInstance().getBattery(batteryHex);
            DecimalFormat df = new DecimalFormat("######0.00");
            battery = Double.valueOf(df.format(battery));
            //保存车位锁电量
            lockRepository.updateBatteryByMac(mac, battery);
            //错误码
            int errcode = Character.getNumericValue(payload.charAt(2));
            //摇臂位置：1- 上锁,2- 解锁,3- 异常s
            int position = Character.getNumericValue(payload.charAt(Constants.LOCK_POSITION_INDEX));
            // 摇臂复位控制源0- 摇臂自动复位,1- 蓝牙控制复位,2- 远程控制复位,3- 超声检测无车复位
            /*int btOrNet = Character.getNumericValue(payload.charAt(4));*/
            //报警码： 0-报警音， 1-没有报警音
            int buzzerState = Character.getNumericValue(payload.charAt(Constants.LOCK_BUZZERSTATE_INDEX));
            // 车位锁使能状态： 0-失能， 1-使能
            int workState = Character.getNumericValue(payload.charAt(Constants.LOCK_WORKSTATE_INDEX));
            Lock lock = lockRepository.findByMac(mac);
            log.info("车位ID，parklocID = " + lock.getParklocId());
            Parkloc parkloc = parklocRepository.findOne(lock.getParklocId());
            if (parkloc != null) {
                Parklot parklot = parklotRepository.findOne(parkloc.getParklotId());
                log.info("车场ID，parklotID = " + parklot.getId());
                if (parklot != null && Constants.PARK_LOT_CHARGE_ONE.equals(parklot.getChargeType())) {
                    try {

//                        String url = configurationFiles.getHttpPrefix().concat(configurationFiles.getServerIpDev()).concat(":")
//                                .concat(configurationFiles.getPort()).concat("/apiwrite/reserve/process");
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("parkLotId", parklot.getId());
                        jsonParams.put("parkLoctId", parkloc.getId());
                        jsonParams.put("tag", position);
                        String result = ribbonTemplate.postForEntity("http://apiwrite/reserve/process",jsonParams,String.class).getBody();
//                        String result = HttpUtil.doPosts(url, jsonParams);
                        log.info("调用修改停车订单接口 响应结果：" + result);
                    } catch (Exception e) {
                        log.error("新增或结束停车订单异常{}", e);
                    }
                }


            }
            log.info("查询车位信息，parkloc = " + parkloc);
            LogLock logLock = new LogLock(lock.getId(), position, buzzerState, errcode, workState, battery, System.currentTimeMillis());
            logLockRepository.save(logLock);
            log.info("车位锁信息保存成功，logLock = " + logLock);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            java.util.concurrent.locks.Lock lock = LockUtil.getInstance().getLock();
            try {
                lock.lock();
                log.info("isComplete: {}", iMqttDeliveryToken.isComplete());
                if (iMqttDeliveryToken.isComplete()) {
                    Condition condition = LockUtil.getInstance().getCondition();
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            log.info("连接mqtt服务器成功，订阅主题");
            subscribe();
        }
    }

    public void subscribe() {
        try {
            mqttClient.subscribe(Constants.LOCK_SUBSCRIBE_PREFIX);
        } catch (MqttException e) {
            log.error("subscribe failed." + e);
        }
    }

    @Override
    public Resp control(LockControlRequest lockControlRequest) {
        java.util.concurrent.locks.Lock lock = LockUtil.getInstance().getLock();
        try {
            lock.lock();
            try {
                //验证时间戳是否合法
                if (CommonUtil.isTimeStampInValid(lockControlRequest.getTimestamp())) {
                    throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
                }
                Integer lockId = lockControlRequest.getLock_id();
                Integer command = lockControlRequest.getCommand();
                Lock lockIot = lockRepository.findOne(lockId);
                if (null == lockIot) {
                    throw new QhieException(Status.ApiErr.NONEXISTENT_LOCK);
                }
                if (null == lockIot.getGatewayId()) {
                    throw new QhieException(Status.ApiErr.NONEXISTENT_GATEWAY);
                }
                Gateway gateway = gatewayRepository.findOne(lockIot.getGatewayId());
                if (null == gateway) {
                    throw new QhieException(Status.ApiErr.NONEXISTENT_GATEWAY);
                }
                String identifier = gateway.getIdentifier();
                String lockMac = lockIot.getMac();
                //如果是升起，先发送使能指令
                if (1 == command) {
                    this.publish(this.getCommand(4), identifier, lockMac);
                }
                String lockCommand = this.getCommand(command);
                this.publish(lockCommand, identifier, lockMac);
                log.info("mac:{},command:{}",lockMac, lockCommand);
                Condition condition = LockUtil.getInstance().getCondition();
                condition.await();
                Integer orderId = lockControlRequest.getOrder_id();
                if(null != orderId && orderId != 0){
                    redisService.setStr(orderId.toString(),"1",60L);
                }
                return RespUtil.successResp(System.currentTimeMillis());
            } catch (InterruptedException e) {
                log.error("system error." + e);
                throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
            }
        }
        catch (QhieException e){
            log.error("system error." + e);
            throw e;
        }finally {
            lock.unlock();
        }
    }



    @Override
    public Resp batchControl(LockBatchControlRequest lockBatchControlRequest){
        java.util.concurrent.locks.Lock lock = LockUtil.getInstance().getLock();
        try {
            lock.lock();
            try {
                //验证时间戳是否合法
                if (CommonUtil.isTimeStampInValid(lockBatchControlRequest.getTimestamp())) {
                    throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
                }
                Integer command = lockBatchControlRequest.getCommand();
                List<Integer> lockIdList = lockBatchControlRequest.getLockList();
                List<Object[]> locks = lockRepository.findLockInfoList(lockIdList);
                //如果是升起，先发送使能指令
                if(1 == command){
                    for(Object[] lockObj : locks){
                        String lockMac = lockObj[0].toString();
                        String identifier = lockObj[1].toString();
                        this.publish(this.getCommand(4), identifier, lockMac);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(Object[] lockObj : locks){
                    String lockMac = lockObj[0].toString();
                    String identifier = lockObj[1].toString();
                    String lockCommand = this.getCommand(command);
                    this.publish(lockCommand, identifier, lockMac);
                    log.info("mac:{},command:{}",lockMac, lockCommand);
                }
                Condition condition = LockUtil.getInstance().getCondition();
                condition.await();
                return RespUtil.successResp(System.currentTimeMillis());
            } catch (InterruptedException e) {
                log.error("system error." + e);
                throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
            }
        }
        catch (QhieException e){
            log.error("system error." + e);
            throw e;
        }finally {
            lock.unlock();
        }
    }

    public String getCommand(Integer command) {
        if (command == null) {
            throw new RuntimeException("command could not be null");
        }
        if (command == 1) {
            return Constants.LOCK_COMMAND_UP;
        }
        else if (command == 2) {
            return Constants.LOCK_COMMAND_DOWN;
        }
        else if (command == 3) {
            return Constants.LOCK_COMMAND_DOWN_DISABLE;
        }
        else if (command == 4) {
            return Constants.LOCK_COMMAND_ENABLE;
        }
        return null;
    }

    public void publish(String command, String identifier, String lockMac) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            String sendMsg = "{\"version\": \"v1\", \"gateway_id\": \"" + identifier
                    + "\",\"type\": \"DEVICE_CMD\", \"device\": {\"payload\": \""
                    + command + "\", \"mac\": \"" + lockMac + "\", \"type\": \"CON\"}}";
            mqttMessage.setPayload(sendMsg.getBytes());
            MqttTopic mqttTopic = mqttClient.getTopic(Constants.LOCK_PUBLISH_PREFIX.concat(identifier));
            mqttTopic.publish(mqttMessage);
        } catch (MqttException e) {
            log.error("publish failed." + e);
        }
    }


    @Override
    public Resp getState(LockGetStateRequest lockGetStateRequest) {
        Integer lockId = lockGetStateRequest.getLock_id();
        Integer command =  lockGetStateRequest.getCommand();
        Long controlTime = lockGetStateRequest.getControl_time();
        Integer controlLogCount = logLockRepository.findLogByLockIdAndRockerStateAndCreateTime(lockId,command,controlTime);
        if(controlLogCount != 0){
            return RespUtil.successResp(1);
        }
        return RespUtil.successResp(0);
    }

    @Override
    public Resp waitTime(OrderIdRequest orderIdRequest) {
        redisService.setStr(orderIdRequest.getOrder_id().toString(),"1",60L);
        return RespUtil.successResp();
    }




}
