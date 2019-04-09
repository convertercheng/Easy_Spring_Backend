package com.qhieco.lock.service.impl;

import com.google.gson.Gson;
import com.qhieco.commonentity.LogLock;
import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Parklot;
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
import com.qhieco.lock.service.NBLOTService;
import com.qhieco.request.api.LockBatchControlRequest;
import com.qhieco.request.api.LockControlRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.NBLockData;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Condition;

//import com.qhieco.apiservice.impl.order.OrderChargingService;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 上午11:04
 * <p>
 * 类说明：
 * NBIOT车位锁具体业务层
 */

@Service
@Slf4j
public class NBIOTLockServiceImpl implements NBLOTService {

    Gson gson = new Gson();
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    @Autowired
    ConfigurationFiles configurationFiles;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    LogLockRepository logLockRepository;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    private RestTemplate ribbonTemplate;

    private String NBIOTHost;

    private static Boolean flag = false;

    public NBIOTLockServiceImpl(    @Value("${NBIOT_HOST}") String NBIOTHost) {
        this.NBIOTHost = NBIOTHost;
        log.info("NBIOTLockServiceImpl init");
        connect();
        subscribe();
        if (parklocRepository == null || logLockRepository == null || lockRepository == null) {
            log.error("parkingDao or parkingService or lockRepository is null");
        }
    }

    public void connect() {
        try {
//            String NBIOTHost = "";
            Properties prop = new Properties();
            FileInputStream applicationIn = null;
            FileInputStream activeIn = null;
            try {
//                String path = LockService.class.getClassLoader().getResource("application.yml").getPath();
//                log.info("启动服务读取application.yml：" + path);
//                applicationIn = new FileInputStream(path);
//                prop.load(applicationIn);
//                String active = prop.getProperty("active");
//                log.info("启动服务链接克莱玛，active =" + active);
//
//                path = LockService.class.getClassLoader().getResource("application-" + active + ".yml").getPath();
//                log.info("启动服务读取application-" + active + ".properties文件路径：" + path);
//                activeIn = new FileInputStream(path);
//                prop.load(activeIn);

//                NBIOTHost = prop.getProperty("NBIOT_HOST");
                log.info("启动服务链接克莱玛，NBIOTHost = " + NBIOTHost);
            } catch (Exception e) {
                log.error("启动服务连接克莱玛异常， " + e);
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
            mqttClient = new MqttClient(NBIOTHost, clientId, new MemoryPersistence());
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            NBIOTCallback nbiotCallback = new NBIOTCallback();
            mqttClient.setCallback(nbiotCallback);
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setKeepAliveInterval(60);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttClient.connect(mqttConnectOptions);
            log.info("NBIOT MQTT连接结果：" + mqttClient.isConnected() + ", clientId=" + clientId);
        } catch (MqttException e) {
            log.error("connect failed." + e);
        }
    }

    private class NBIOTCallback implements MqttCallbackExtended {
        @Override
        public void connectionLost(Throwable throwable) {
            log.error("NBIOTLock MQTT the connection was lost.");
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
            NBLockData nbLockData = gson.fromJson(json, NBLockData.class);
            String type = nbLockData.getType();
            String macId = nbLockData.getMacid();
            String status = nbLockData.getStatus();
            String enable = nbLockData.getEnable();
            String vBat = nbLockData.getVBat();
            Double battery = LockUtil.getInstance().getBattery(vBat);
            log.info("进入NB车锁通知，nbLockData{}",nbLockData);
            if (!Constants.LOCK_DATA.equals(type)) {
                return;
            }
            Lock lock = lockRepository.findByMac(macId);
            Parkloc parkloc = null;
            parkloc = parklocRepository.findOne(lock.getParklocId());
            Parklot parklot = null;
            if (parkloc != null) {
                parklot = parklotRepository.findOne(parkloc.getParklotId());
            }
            int position;
            if ("up".equals(status)) {
                position = 1;
            } else {
                position = 2;
            }
            if (parklot != null && Constants.PARK_LOT_CHARGE_ONE.equals(parklot.getChargeType())) {
                try {

                    log.info("车场ID，parklotID = " + parklot.getId());
                    if (parklot != null && Constants.PARK_LOT_CHARGE_ONE.equals(parklot.getChargeType())) {
//                        String url = configurationFiles.getHttpPrefix().concat(configurationFiles.getServerIpDev()).concat(":")
//                                .concat(configurationFiles.getPort()).concat("/apiwrite/reserve/process");
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("parkLotId", parklot.getId());
                        jsonParams.put("parkLoctId", parkloc.getId());
                        jsonParams.put("tag", position);
                        String result = ribbonTemplate.postForEntity("http://apiwrite/reserve/process",jsonParams,String.class).getBody();
//                        String result = HttpUtil.doPosts(url, jsonParams);
                        log.info("调用修改停车订单接口 响应结果：" + result);
                    }

                } catch (Exception e) {
                    log.error("新增或结束停车订单异常{}", e);
                }
            }

            LogLock logLock = new LogLock(lock.getId(), position, 2, 0, Integer.valueOf(enable), battery, System.currentTimeMillis());
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
            mqttClient.subscribe(Constants.NB_IOT_LOCK_SUBSCRIBE_PREFIX);
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
                String lockMac = lockIot.getMac();
                String lockCommand = this.getCommand(command);
                this.publish(lockCommand, lockMac);
                log.info("mac:{},command:{}",lockMac, lockCommand);
                Condition condition = LockUtil.getInstance().getCondition();
                condition.await();
                return RespUtil.successResp();
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

                List<Lock> locks = lockRepository.findAll(lockBatchControlRequest.getLockList());

                for(Lock lockObj : locks){
                    String lockMac = lockObj.getMac();
                    String lockCommand = this.getCommand(lockBatchControlRequest.getCommand());
                    this.publish(lockCommand, lockMac);
                    log.info("mac:{},command:{}",lockMac, lockCommand);
                }
                Condition condition = LockUtil.getInstance().getCondition();
                condition.await();
                return RespUtil.successResp(System.currentTimeMillis());
            } catch (InterruptedException e) {
                log.error("system error." + e);
                throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
            }
        }catch (QhieException e){
            log.error("system error." + e);
                throw e;
        }
        finally {
            lock.unlock();
        }
    }

    public String getCommand(Integer command) {
        if (command == null) {
            throw new RuntimeException("command could not be null");
        }
        if (command == 1) {
            return Constants.LOCK_COMMAND_UP;
        } else if (command == 2) {
            return Constants.LOCK_COMMAND_DOWN;
        }
        return null;
    }

    public void publish(String command, String lockMac) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            String sendMsg = "{\"macid\":" + lockMac + ",\"action\":" + command + ",\"msg_id\":\"1\",\"type\":\"DATA\"}";
            mqttMessage.setPayload(sendMsg.getBytes());
            MqttTopic mqttTopic = mqttClient.getTopic(Constants.NB_IOT_LOCK_PUBLISH_PREFIX);
            mqttTopic.publish(mqttMessage);
        } catch (MqttException e) {
            log.error("publish failed.", e);
        }
    }


}
