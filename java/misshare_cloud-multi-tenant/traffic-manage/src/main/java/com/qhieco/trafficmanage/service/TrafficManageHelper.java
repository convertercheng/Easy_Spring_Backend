package com.qhieco.trafficmanage.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectResult;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.trafficmanage.entity.TrafficManageType;
import com.qhieco.trafficmanage.entity.request.BaseRequest;

import com.qhieco.trafficmanage.entity.request.CarAccessUploadRequest;
import com.qhieco.trafficmanage.entity.request.ParklotSceneUploadRequest;

import com.qhieco.util.HttpUtil;
import com.qhieco.util.OrderNoGenerator;
import com.qhieco.util.RSAUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import lombok.Getter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.Request;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 下午2:18
 * <p>
 * 类说明：
 * ${description}
 */
@Component
@Slf4j
public class TrafficManageHelper {

    @Value("${PLATFORM_BASEURL}")
    private String BASEURL;
    @Value("${PLATFORM_PUBLICKEY}")
    private String publicKey;
    @Value("${PLATFORM_PRIVATEKEY}")
    private String privateKey;
    @Value("${KEY_RSA_SIGNATURE}")
    private String KEY_RSA_SIGNATURE;

    @Getter
    @Value("${SCID}")
    private String SCID;

    @Value("${OBS_END_POINT}")
    private String OBS_END_POINT;
    @Value("${OBS_AK}")
    private String OBS_AK;
    @Value("${OBS_SK}")
    private String OBS_SK;
    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;

    private static final Gson gson = new Gson();
    private static final String MAC_KEY = "mac";
    //V1.4
    private static final String MSGVER = "V1.4";

    public <T extends BaseRequest, E> E sendMessage(T request, Class<E> respClass){

        Class clazz = request.getClass();
        TrafficManageType config = TrafficManageType.find(clazz);
        request.setCSID(SCID);
        request.setMSGVER(MSGVER);
        request.setQQSJ(TimeUtil.timestamp2Str(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
        if(request.getCSPTLS()==null) {
            request.setCSPTLS(OrderNoGenerator.getOrderNo(config.getType(), "0"));
        }
        request.setJYLX(config.getJYLX());
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        JsonObject jsonObject = new JsonParser().parse(gson.toJson(request)).getAsJsonObject();
        JsonObject jsonObjectParsed = new JsonObject();
        jsonObject.entrySet().forEach(entry->{
            jsonObjectParsed.addProperty(entry.getKey(),entry.getValue().toString());

        });
        Map<String, Object> originMap = gson.fromJson(gson.toJson(jsonObjectParsed), type);
        Map<String, Object> sortedMap = new TreeMap<>(Comparator.naturalOrder());
        sortedMap.putAll(originMap);
        StringBuilder stringBuilder = new StringBuilder();
        sortedMap.forEach((k,v)->{
            stringBuilder.append(k);
            stringBuilder.append(v.toString());
        });

        String UnSignedMsg = stringBuilder.toString();
        RSAUtil rsaUtil = new RSAUtil(publicKey,privateKey);
        //生成mac签名信息并将这个信息添加到sortedMap中
        String macSign = rsaUtil.generateSign(UnSignedMsg,this.KEY_RSA_SIGNATURE);
        sortedMap.put(MAC_KEY,macSign);
        String sendJson = gson.toJson(sortedMap);
        String resp = HttpUtil.doPost(BASEURL+"/"+config.getUrl(),sendJson);
        return gson.fromJson(resp, respClass);
    }

    //默认地区信息
    private static final String DEFAULT_REGION_INFO = "_0_0_0_0" ;

    //停车场信息的文件夹名称
    private static final String PARKLOT_PATH = "psnmng";

    /**
     *
     * @param request
     * @return
     */

    public Resp uploadParklotInfo(ParklotSceneUploadRequest request){
        String filename = request.getSceneImage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PARKLOT_PATH);
        stringBuilder.append("/");
        stringBuilder.append(request.getTCCID());
        String sendFileName = stringBuilder.toString();
        return obsUpload(filename,sendFileName);
    }

    /**
     *
     * @param request 请求参数
     * @return
     */
    public Resp uploadCarAccessInfo( CarAccessUploadRequest request){

        //获取要上传图片的文件名
        String filename = request.getSceneImage();
        //生成上传对象的文件路径
        //生成文件路径名
        SimpleDateFormat simpleDateFilePathFormat = new SimpleDateFormat("yyyyMMdd");
        String pathname = simpleDateFilePathFormat.format(request.getTimestamp());
        //生成文件中的时间字段
        SimpleDateFormat simpleDateFilenameFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        String requestTimeInfo = simpleDateFilenameFormat.format(request.getTimestamp());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pathname);
        stringBuilder.append("/");
        stringBuilder.append(requestTimeInfo);
        stringBuilder.append(request.getMonitoringPoints());
        stringBuilder.append(request.getLane());
        stringBuilder.append(request.getImageIndex());
        //如果未上传地区信息，则使用默认地区信息
        if(request.getRegionInfo() == null){
            stringBuilder.append(DEFAULT_REGION_INFO);
        }else{
            stringBuilder.append(request.getRegionInfo());
        }
        String sendFileName = stringBuilder.toString();


        return obsUpload(filename,sendFileName);
    }


    /**
     * 集成华为云SDK进行图片上传业务
     * @param filename
     * @param sendFilename
     * @return
     */
    private Resp<String> obsUpload(String filename, String sendFilename){

// 创建ObsClient实例
        Resp<String> resp;
        ObsClient obsClient = null;
        try
        {
            obsClient = new ObsClient(OBS_AK, OBS_SK, OBS_END_POINT);
            PutObjectResult result = obsClient.putObject(BUCKET_NAME, sendFilename,new File(filename));
            resp = RespUtil.successResp(result.getObjectKey());
        }
        catch (ObsException e)
        {
            log.error("Response Code: " + e.getResponseCode()+
                    "Error Message: " + e.getErrorMessage()+
                    "Error Code:" + e.getErrorCode()+
                    "Request ID:" + e.getErrorRequestId()+
                    "Host ID:" + e.getErrorHostId());
            resp = RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_UPLOAD_FAILD.getCode(),e.getErrorMessage());
        }finally{
            // 关闭obsClient
            if(obsClient != null){
                try
                {
                    obsClient.close();
                }
                catch (IOException ignored)
                {
                }
            }
        }
        return resp;
    }

    /**
     * 上传图片方法
     * @param url　桶名加华为云地址
     * @param filename　
     * @return
     */
    private Resp uploadImg(String url, String filename, String sendFileName){

        /**
         * 桶名生成　url 前缀
         */
        String hostUrl = BUCKET_NAME + "." + url;
        System.out.println(hostUrl);

        /**
         * 信息上传为停车场id TCCID　通行记录为图片存储使用通过日期(YYYYMMDD，如果是 2017
         年 7 月 1 日通过的，则为 20170701/pic.jpg)为目录
         */
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Date",TimeUtil.timeStamp2Date(System.currentTimeMillis(), null));

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", sendFileName);

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put("file", new File(filename));

        try {
            Resp resp = HttpUtil.post(hostUrl, headers ,params, "file", fileMap);
            if(resp.getError_code().equals(Status.WebErr.SUCCESS.getCode())){
                return resp;
            }else {
                return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),parseError(resp.getError_message()));
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseError(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new ByteArrayInputStream(message.getBytes("utf-8"))));
        StringBuilder stringBuilder = new StringBuilder();
        NodeList codeNodes = document.getElementsByTagName("Code");
        if(codeNodes.getLength()==0){
            return stringBuilder.toString();
        }
        Node codeNode = codeNodes.item(0);
        stringBuilder.append("Code:");
        stringBuilder.append(codeNode.getTextContent());
        NodeList messageNodes = document.getElementsByTagName("Message");
        if(messageNodes.getLength()==0){
            return stringBuilder.toString();
        }
        Node messageNode = messageNodes.item(0);
        stringBuilder.append("; Message:");
        stringBuilder.append(messageNode.getTextContent());
        return stringBuilder.toString();
    }

}
