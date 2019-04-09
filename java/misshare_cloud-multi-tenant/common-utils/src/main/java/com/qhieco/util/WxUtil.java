package com.qhieco.util;

import com.google.gson.Gson;
import com.qhieco.constant.Constants;
import com.qhieco.response.data.api.WxSmallRoutineData;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/25 17:36
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
public class WxUtil {
    /**
     * 微信模板推送接口地址
     */
    public  static String URL="https://api.weixin.qq.com/cgi-bin/message/template/send";


    /**
     * 获取openId
     * @param code
     * @return
     * @throws Exception
     */
    public static Map<String,String> getOAuthOpenId(String code,String appid,String  secret) throws Exception {
        String openId=null;
        RestTemplate restTemplate=new RestTemplate();
        String requestUrl = Constants.WXPAY_AUTHOR_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        String jsonObject = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject1=null;
        if (null != jsonObject) {
            try {
                jsonObject1=JSONObject.fromObject(jsonObject);
                log.info("getOAuthOpenId {}",jsonObject1);
                openId=jsonObject1.getString("openid");
                return  getOAuthUserInfo(openId,jsonObject1.getString("access_token"));
            } catch (JSONException e) {
                log.error("网页授权获取openId失败 errcode:{} errmsg:{}", jsonObject1.getInt("errcode"), jsonObject1.getString("errmsg"));
            }
        }
        return null;
    }

    /**
     * 获取小程序openId
     * @param code
     * @return
     * @throws Exception
     */
    public static String getOAuthXcxOpenId(String code,String appid,String  secret) throws Exception {
        String openId=null;;
        String requestUrl = Constants.WXPAY_XCX_AUTHOR_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        String jsonObject = HttpUtil.doGet(requestUrl);
        Map<String,String> map=new HashMap<>();
        JSONObject jsonObject1=null;
        if (null != jsonObject) {
            try {
                jsonObject1=JSONObject.fromObject(jsonObject);
                log.info("getOAuthOpenId {}",jsonObject1);
                openId=jsonObject1.getString("openid");
                return  openId;
            } catch (JSONException e) {
                log.error("网页授权获取openId失败 errcode:{} errmsg:{}", jsonObject1.getInt("errcode"), jsonObject1.getString("errmsg"));
            }
        }
        return null;
    }

    /**
     * 获取小程序openId unionId
     * @param code
     * @return
     * @throws Exception
     */
    public static WxSmallRoutineData getSmallRoutineAuthor(String code,String appid,String  secret) throws Exception {
        String requestUrl = Constants.WXPAY_XCX_AUTHOR_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        String jsonObject = HttpUtil.doGet(requestUrl);
        JSONObject result=null;
        if (null != jsonObject) {
            try {
                result=JSONObject.fromObject(jsonObject);
                log.info("getOAuthOpenId {}",result);
                Gson gson = new Gson();
                WxSmallRoutineData wxSmallRoutineData = gson.fromJson(result.toString(), WxSmallRoutineData.class);
                return wxSmallRoutineData;
            } catch (JSONException e) {
                log.error("网页授权获取openId失败 errcode:{} errmsg:{}", result.getInt("errcode"), result.getString("errmsg"));
            }
        }
        return null;
    }

    /**
     * 获取openId
     * @param openId
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String,String> getOAuthUserInfo(String openId, String token ) throws Exception {
        Map<String,String> map=new HashMap<>();
        String url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        RestTemplate restTemplate=new RestTemplate();
        String requestUrl = url.replace("ACCESS_TOKEN", token).replace("OPENID", openId);
        String jsonObject = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject1=null;
        String unionid="";
        if (null != jsonObject) {
            try {
                jsonObject1=JSONObject.fromObject(jsonObject);
                log.info("getOAuthUserInfo {}",jsonObject1);
                unionid=jsonObject1.getString("unionid");
                map.put("unionId",unionid);
                map.put("openId",openId);
            } catch (JSONException e) {
                log.error("网页授权获取unionid失败 errcode:{} errmsg:{}", jsonObject1.getInt("errcode"), jsonObject1.getString("errmsg"));
            }
        }
        return map;
    }

    /**
     * 获取accessToken
     * @param appId
     * @param secret
     * @return
     * @throws Exception
     */
    public static Map<String,String> getAccessToken(String appId,String secret) {
        String accessTokenUrl = Constants.WX_ACCESS_TOKEN_URL.replace("APPID", appId).
                replace("APPSECRET", secret);
        String jsonObject = HttpUtil.doGet(accessTokenUrl);
        JSONObject tokenResult = null;
        Map<String ,String> tokenMap=new HashMap<>();
        if (null != jsonObject) {
            try {
                tokenResult = JSONObject.fromObject(jsonObject);
                log.info("tokenResult {}", tokenResult);
                tokenMap.put("accessToken", tokenResult.getString("access_token"));
                tokenMap.put("expiresIn",tokenResult.getString("expires_in"));
                return tokenMap;
            } catch (JSONException e) {
                log.error("获取access_token失败 errcode:{} errmsg:{}", tokenResult.getInt("errcode"), tokenResult.getString("errmsg"));
            }
        }
        return null;
    }
}
