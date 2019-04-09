package com.qhieco.push;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

import java.util.List;

public class QhAnnouncePushPayload extends QhPushPayload{

    private static final String NOTIFICATION_TITLE = "平台公告";

    private List<String> mRegisterId;

    @Override
    protected String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public PushPayload getPushPayLoad(String messageTemplate, String placeHolder) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(mRegisterId))
                .setNotification(Notification.alert(String.format(messageTemplate, placeHolder)))
                .build();
    }

    @Override
    public PushPayload getPushPayLoad(String messageTemplate, String placeHolder,String btName,String btPassword,String btNumber) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(mRegisterId))
                .setNotification(Notification.alert(String.format(messageTemplate, placeHolder)))
                .build();
    }

    QhAnnouncePushPayload(List<String> registerId) {
        this.mRegisterId = registerId;
    }
}
