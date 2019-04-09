package com.qhieco.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

/**
 * Created by xujiayu on 17/9/17.
 */
public class Signature {
    private static Logger log = LoggerFactory.getLogger(Signature.class);

    public static String getSign(String string, String key) {
        try {
            String stringSignTemp = string.concat("&key=").concat(key);
            String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes("UTF-8")).toUpperCase();
            return sign;
        } catch (Exception e) {
            log.error("get sign failed." + e);
            return null;
        }
    }
}
