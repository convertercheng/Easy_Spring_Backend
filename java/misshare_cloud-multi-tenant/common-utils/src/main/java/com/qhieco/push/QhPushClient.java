package com.qhieco.push;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class QhPushClient {

    private static final String TAG = QhPushClient.class.getSimpleName();

    private static final String MASTER_SECRET = "c69459cc73d7add9d732ad4d";
    private static final String APP_KEY = "a894e959f70ce5f6fa4dc9fc";

    private JPushClient mQhPushClient;

    private QhPushClient(){
        log.info("{} is created", TAG);
        mQhPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
    }

    private static class QhPushClientHolder {
        private static final QhPushClient mQhPushClient = new QhPushClient();
    }

    public static QhPushClient getInstance() {
        return QhPushClientHolder.mQhPushClient;
    }

    public JPushClient getQhPushClient() {
        return mQhPushClient;
    }

}
