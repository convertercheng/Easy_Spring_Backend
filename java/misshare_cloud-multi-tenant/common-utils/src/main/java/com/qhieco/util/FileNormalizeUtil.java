package com.qhieco.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/6/6 上午10:57
 * <p>
 * 类说明：
 *     文件上传的存入文件表的工具类
 *     用法：
 *         FileNormalizeUtil.FileNormalize fileNormalize = FileNormalizeUtil.getFileNormalize(file, type);
 *         String name = fileNormalize.getName()
 *         String path = fileNormalize.getPath()
 *         String intro = fileNormalize.getIntro()
 */
@Slf4j
public class FileNormalizeUtil {

    public static FileNormalize getFileNormalize(MultipartFile file, FileType type) {
        FileNormalize fileNormalize = new FileNormalize();
        String name = file.getOriginalFilename();
        fileNormalize.setName(name);
        String path = getDirName(type)
                + System.currentTimeMillis() + (int)(Math.random() * 900 + 100) + name.substring(name.lastIndexOf("."));
        fileNormalize.setPath(path);
        String intro = getMd5(file);
        fileNormalize.setIntro(intro);
        return fileNormalize;
    }

    private static String getDirName(FileType type) {
        String dir;
        switch (type) {
            case AVATAR:
                dir = "avatar/";
                break;
            case ACTIVITY:
                dir = "activity/";
                break;
            case ADVERT:
                dir = "advert/";
                break;
            case APP:
                dir = "app/";
                break;
            case FEEDBACK:
                dir = "feedback/";
                break;
            case INVOICE:
                dir = "invoice/";
                break;
            case PARKLOT:
                dir = "parklot/";
                break;
            case WITHDRAW:
                dir = "withdraw/";
                break;
            case ORDER:
                dir = "order/";
                break;
            case WX:
                dir = "wx/";
                break;
            default:
                dir = "default/";
                break;
        }
        return dir;
    }

    public enum FileType {
        /**
         * 头像目录
         */
        AVATAR,
        /**
         * 活动目录
         */
        ACTIVITY,
        /**
         * 广告目录
         */
        ADVERT,
        /**
         * app包目录
         */
        APP,
        /**
         * 反馈目录
         */
        FEEDBACK,
        /**
         * 发票目录
         */
        INVOICE,
        /**
         * 车场目录
         */
        PARKLOT,
        /**
         * 提现目录
         */
        WITHDRAW,
        /**
         * 订单目录
         */
        ORDER,
        /**
         * 微信目录
         */
        WX,
        /**
         * 默认目录
         */
        DEFAULT
    }

    /**
     * 获取上传文件的MD5值
     * @param file 上传文件
     * @return 文件内容的md5值
     */
    private static String getMd5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    public static class FileNormalize {
        private String name;
        private String path;
        private String intro;

        void setName(String name) {
            this.name = name;
        }

        void setPath(String path) {
            this.path = path;
        }

        void setIntro(String intro) {
            this.intro = intro;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String getIntro() {
            return intro;
        }
    }
}
