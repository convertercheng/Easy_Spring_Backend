package com.qhieco.push;

import cn.jpush.api.push.model.PushPayload;
import com.qhieco.commonentity.OrderParking;

public abstract class QhPushPayload {

    public static final String JPUSH_EXTRA_EXTRA = "cn.jpush.android.EXTRA";

    protected abstract String getNotificationTitle();

    public abstract PushPayload getPushPayLoad(String messageTemplate, String placeHolder);

    public abstract PushPayload getPushPayLoad(String messageTemplate, String placeHolder,String btName,String btPassword,String btNumber);


}
