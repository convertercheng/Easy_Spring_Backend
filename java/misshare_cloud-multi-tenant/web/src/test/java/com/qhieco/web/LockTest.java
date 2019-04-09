package com.qhieco.web;

import com.qhieco.commonrepo.ParklocRepository;
import com.qhieco.request.web.LockRequest;
import com.qhieco.util.HttpUtil;
import com.qhieco.webservice.impl.LockServiceImpl;
import com.qhieco.webservice.impl.wx.WxService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.sf.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.*;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-16 下午4:48
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LockTest {
    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    LockServiceImpl lockService;

    @Autowired
    WxService wxService;

    @Test
    public void testDao()throws IOException{
        String imei ="867186032552993";
        String token = wxService.replaceXcxAccessToken();   // 得到token
        Map<String, Object> params = new HashMap<>();
        params.put("scene", imei);  //参数
        params.put("page", "page/msg_waist/msg_waist"); //位置
        params.put("width", 430);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token);  // 接口
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSONObject.fromObject(params).toString();           //必须是json模式的 post
        StringEntity entity;
        try {
            entity = new StringEntity(body);
            entity.setContentType("image/png");
            httpPost.setEntity(entity);
            HttpResponse response;
            response = httpClient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            String name = imei+".png";
            File targetFile = new File("D:\\");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            FileOutputStream out = new FileOutputStream("D:\\upload\\5.png");
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            while((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }

    @Test
    public void query(){
        val req = new LockRequest();
        req.setSEcho(1);
        req.setLength(10);
        req.setStart(0);
        req.setPhone("150");
        System.out.println(lockService.query(req));
    }
}
