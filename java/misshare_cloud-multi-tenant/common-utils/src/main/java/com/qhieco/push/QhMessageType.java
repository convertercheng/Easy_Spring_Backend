package com.qhieco.push;

public enum QhMessageType {
    RESERVE("预约消息"),
    WALLET("钱包消息"),
    ANNOUNCEMENT("平台公告"),
    CUSTOM("自定义消息");

    private String mTitle;

    QhMessageType(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
