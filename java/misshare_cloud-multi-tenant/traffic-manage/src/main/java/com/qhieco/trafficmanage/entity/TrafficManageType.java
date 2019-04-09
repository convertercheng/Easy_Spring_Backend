package com.qhieco.trafficmanage.entity;

import com.qhieco.trafficmanage.entity.request.CancelSignRequest;
import com.qhieco.trafficmanage.entity.request.DelayEnterRequest;
import com.qhieco.trafficmanage.entity.request.EnterRequest;
import com.qhieco.trafficmanage.entity.request.LeaveRequest;
import com.qhieco.trafficmanage.entity.request.LockPayQueryRequest;
import com.qhieco.trafficmanage.entity.request.LockPayRequest;
import com.qhieco.trafficmanage.entity.request.ParklotInfoRequest;
import com.qhieco.trafficmanage.entity.request.PayInfoRequest;
import com.qhieco.trafficmanage.entity.request.PhotoRequest;
import com.qhieco.trafficmanage.entity.request.RepayInfoRequest;
import com.qhieco.trafficmanage.entity.request.ReuploadCarInOutPicRequest;
import com.qhieco.trafficmanage.entity.request.SignRequest;
import com.qhieco.trafficmanage.entity.request.UnlockPayRequest;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-22 下午3:54
 * <p>
 * 类说明：
 * ${description}
 */
public enum TrafficManageType {
        PARKINFO(0, "psnmng", "psnupload",ParklotInfoRequest.class),
        ENTER(1, "carinupload", "carinupload",EnterRequest.class),
        DELAYENTER(2,"carinupload", "ispayproxy",DelayEnterRequest.class ),
        LEAVE(3,"caroutupload","caroutupload", LeaveRequest.class),
        PHOTO(4,"reuploadcarinoutpic","reuploadcarinoutpic", PhotoRequest.class),
        PAY_INFO(5,"payinfoupload","payinfoupload", PayInfoRequest.class),
        REPAY_INFO(6,"repayinfoupload","repayinfoupload", RepayInfoRequest.class),
        LOCK_PAY_QUERY(7,"lockpayproxyquery","lockpayproxyquery", LockPayQueryRequest.class),
        LOCK_PAY(8,"lockpayproxy","lockpayproxy",LockPayRequest.class),
        UNLOAD_PAY(9,"unlockpayproxy","unlockpayproxy", UnlockPayRequest.class),
        SIGN(10,"sign","sign", SignRequest.class),
        CANCELSIGN(11,"cancelsign","cancelsign", CancelSignRequest.class),
        REUPLOAD_CAR_INOUT_PIC(12,"reuploadcarinoutpic","reuploadcarinoutpic",ReuploadCarInOutPicRequest.class);
        @Getter
        private int type;
        @Getter
        private String url;
        @Getter
        private String JYLX;
        @Getter
        private Class aClass;

        TrafficManageType(int type, String msg, String JYLX, Class aClass) {
            this.type = type;
            this.url = msg;
            this.JYLX = JYLX;
            this.aClass = aClass;
        }

        private static final Map<Class, TrafficManageType> lookup = new HashMap<>();

        static {
            for (TrafficManageType e : EnumSet.allOf(TrafficManageType.class)) {
                lookup.put(e.aClass,e);
            }
            System.out.println(lookup);
        }

        public static TrafficManageType find(Class value) {
            return lookup.get(value);
        }

}
