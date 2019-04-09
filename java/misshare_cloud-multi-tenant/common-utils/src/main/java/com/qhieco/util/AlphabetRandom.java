package com.qhieco.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * @author xujiayu
 */
public class AlphabetRandom {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static String random(int length) {
        String string = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(string.charAt(secureRandom.nextInt(string.length())));
        }
        return stringBuilder.toString();
    }
}
