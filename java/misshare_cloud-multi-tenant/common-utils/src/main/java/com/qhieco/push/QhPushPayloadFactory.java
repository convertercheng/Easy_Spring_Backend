package com.qhieco.push;

import java.util.List;

public class QhPushPayloadFactory {

    public static QhPushPayload getInstance(String registerId, QhMessageType qhMessageType) {
        if (qhMessageType != null) {
            switch (qhMessageType) {
                case RESERVE:
                    return new QhReservePushPayload(registerId);
                case WALLET:
                    return new QhWalletPushPayload(registerId);
                case CUSTOM:
                    return new QhCustomPushPayload(registerId);
                default:
                    break;
            }
        }
        throw new RuntimeException("QhMessageType cannot be null");
    }

    public static QhPushPayload getInstanceBatch(List<String> registerId, QhMessageType qhMessageType) {
        if (qhMessageType != null) {
            switch (qhMessageType) {
                case ANNOUNCEMENT:
                    return new QhAnnouncePushPayload(registerId);
                default:
                    break;
            }
        }
        throw new RuntimeException("QhMessageType cannot be null");
    }
}
