package com.qhieco.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.qhieco.push.QhMessageType;
import com.qhieco.push.QhPushClient;
import com.qhieco.push.QhPushPayload;
import com.qhieco.push.QhPushPayloadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class QhPushUtil {

    private static final String TAG = QhPushUtil.class.getSimpleName();


    private QhPushUtil(){}

    private static class QhPushUtilHolder {
        private static final QhPushUtil quPushUtil = new QhPushUtil();
    }

    public static QhPushUtil getInstance() {
        return QhPushUtilHolder.quPushUtil;
    }

    public void sendQhPush(String jpushRegId, QhMessageType qhMessageType, String qhMessageTemplate, String placeHolder ) {
        JPushClient jPushClient = QhPushClient.getInstance().getQhPushClient();
        QhPushPayload qhPushPayload = QhPushPayloadFactory.getInstance(jpushRegId, qhMessageType);
        PushPayload pushPayload = qhPushPayload.getPushPayLoad(qhMessageTemplate, placeHolder);
        try {
            PushResult pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
        } catch (APIRequestException e){
        }
    }

    public void sendQhPush(String jpushRegId, QhMessageType qhMessageType, String qhMessageTemplate, String placeHolder, String btName,String btPassword,String btNumber ) {
        JPushClient jPushClient = QhPushClient.getInstance().getQhPushClient();
        QhPushPayload qhPushPayload = QhPushPayloadFactory.getInstance(jpushRegId, qhMessageType);
        PushPayload pushPayload = qhPushPayload.getPushPayLoad(qhMessageTemplate, placeHolder,btName,btPassword,btNumber);
        try {
            PushResult pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
        } catch (APIRequestException e){
        }
    }


    public void sendQhPushBatch(List<String> jpushRegId, QhMessageType qhMessageType, String qhMessageTemplate, String placeHolder) {
        JPushClient jPushClient = QhPushClient.getInstance().getQhPushClient();
        QhPushPayload qhPushPayload = QhPushPayloadFactory.getInstanceBatch(jpushRegId, qhMessageType);
        PushPayload pushPayload = qhPushPayload.getPushPayLoad(qhMessageTemplate, placeHolder);
        try {
            PushResult pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
        } catch (APIRequestException e){
        }
    }

}
