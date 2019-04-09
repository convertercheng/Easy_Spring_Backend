package com.qhieco.push;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.qhieco.constant.Constants;

/**
 * @author  刘江茳
 * 自定义消息生产线
 */
public class QhCustomPushPayload extends QhPushPayload {

    private static final String NOTIFICATION_TITLE = "自定义消息";

    private String mRegisterId;

    @Override
    protected String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public PushPayload getPushPayLoad(String messageTemplate, String placeHolder) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(mRegisterId))
                .setMessage(Message.newBuilder()
                        .setMsgContent(messageTemplate)
                        .addExtra(Constants.ORDER_PARKING_ID,placeHolder)
                        .build())
                .build();
    }

    @Override
    public PushPayload getPushPayLoad(String messageTemplate, String placeHolder,String btName,String btPassword,String btNumber) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(mRegisterId))
                .setMessage(Message.newBuilder()
                        .setMsgContent(messageTemplate)
                        .addExtra(Constants.ORDER_PARKING_ID,placeHolder)
                        .addExtra("btName",btName)
                        .addExtra("btPassword",btPassword)
                        .addExtra("btNumber",btNumber)
                        .build())
                .build();
    }


    QhCustomPushPayload(String registerId) {
        this.mRegisterId = registerId;
    }

}
