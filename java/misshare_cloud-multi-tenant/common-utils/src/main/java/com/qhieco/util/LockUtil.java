package com.qhieco.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xujiayu on 17/10/11.
 */
public class LockUtil {

    private static final String TAG = LockUtil.class.getSimpleName();

    private Logger mLog = LoggerFactory.getLogger(this.getClass());

    private static final class LockUtilHolder {
        private static final LockUtil lockUtil = new LockUtil();
    }

    private Lock mLock;
    private Condition mCondition;

    private LockUtil() {
        mLock = new ReentrantLock();
        mCondition = mLock.newCondition();
    }

    public static LockUtil getInstance() {
        return LockUtilHolder.lockUtil;
    }

    public Lock getLock() {
        return mLock;
    }

    public Condition getCondition() {
        return mCondition;
    }

    public Double getBattery(String voltageHex) {
        Double battery;
        Double voltage = Integer.parseInt(voltageHex, 16) * 0.01289 + 0.3;
        if (voltage < 4) {
            battery = 0.0;
        }
        else {
            battery = (0.8 - 0.1) / (5.2 - 4) * (voltage - 4) + 0.1;
            if (battery >= 1) {
                battery = 1.0;
            }
        }
        return battery;
    }
}
