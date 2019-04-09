package com.qhieco.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.qhieco.constant.Constants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/26 19:47
 * <p>
 * 类说明：
 * ${description}
 */
public class FileUploadUtils {

    /**
     * 上传图片
     * @param path
     * @param base64
     * @return
     */
    public static String uploadImage(String path, String base64,String fileName) {
        File files=new File(path);
        String ret_fileName = null;// 返回给前端已修改的图片名称
        if(!files.exists()){
            files.mkdirs();
        }
        if(StringUtils.isEmpty(base64)){
            return null;
        }
        base64 = base64.replaceAll("data:image/jpeg;base64,", "");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            ret_fileName = new String((fileName+".jpg").getBytes("gb2312"), "ISO8859-1" ) ;
            File file = new File(path + ret_fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
            return path + ret_fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * @Title: GetImageStrFromPath
     * @Description: TODO(将一张本地图片转化成Base64字符串)
     * @param imgPath
     * @return
     */
    public static String GetImageStrFromPath(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 清除文件
     *
     * @param path
     */
    public static void clear(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }


}
