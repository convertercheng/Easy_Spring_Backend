package com.qhieco.apiservice.impl.wx;

import com.qhieco.apiservice.impl.redis.RedisService;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.util.HttpReqUtil;
import com.qhieco.util.JsonUtils;
import com.qhieco.util.TemplateMsgResult;
import com.qhieco.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/17 9:16
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class WxService implements CommandLineRunner {
    @Autowired
    private RedisService redisService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Override
    public void run(String... strings) throws Exception {
        Map<String,String> tokenResultMap= WxUtil.getAccessToken(configurationFiles.getWxMpAppId(),configurationFiles.getWxMpSecret());
        if(tokenResultMap==null){
            log.error("get tokenResultMap wechat error info....");
            return;
        }
        Long expiresInMinute=Long.valueOf(tokenResultMap.get("expiresIn"));
        redisService.setStr("accessToken_"+configurationFiles.getWxMpAppId(),tokenResultMap.get("accessToken"), expiresInMinute);
        Map<String,String> tokenXcxResultMap= WxUtil.getAccessToken(configurationFiles.getWxXcxAppId(),configurationFiles.getWxXcxSecret());
        Long expiresInXcxMinute=Long.valueOf(tokenXcxResultMap.get("expiresIn"));
        if(tokenXcxResultMap==null){
            log.error("get tokenXcxResultMap wechat error info....");
            return;
        }
        redisService.setStr("accessTokenXcx_"+configurationFiles.getWxXcxAppId(),tokenXcxResultMap.get("accessToken"), expiresInXcxMinute);
    }

    /**
     * 替换公众号token
     * @return token
     */
    public String replaceAccessToken(){
        boolean flag=false;
        String accessToken=redisService.getStr("accessToken_"+configurationFiles.getWxMpAppId());
        if(StringUtils.isEmpty(accessToken)){
            flag=true;
        }
        if(flag){
            Map<String,String> tokenResultMap=WxUtil.getAccessToken(configurationFiles.getWxMpAppId(),configurationFiles.getWxMpSecret());
            Long expiresInMinute=Long.valueOf(tokenResultMap.get("expiresIn"));
            redisService.setStr("accessToken_"+configurationFiles.getWxMpAppId(),tokenResultMap.get("accessToken"),expiresInMinute);
        }
        return  redisService.getStr("accessToken_"+configurationFiles.getWxMpAppId());
    }

    /**
     * 替换微信小程序token
     * @return token
     */
    public String replaceXcxAccessToken(){
        boolean flag=false;
        String accessToken=redisService.getStr("accessTokenXcx_"+configurationFiles.getWxXcxAppId());
        if(StringUtils.isEmpty(accessToken)){
            flag=true;
        }
        if(flag){
            Map<String,String> tokenResultMap=WxUtil.getAccessToken(configurationFiles.getWxXcxAppId(),configurationFiles.getWxXcxSecret());
            Long expiresInMinute=Long.valueOf(tokenResultMap.get("expiresIn"));
            redisService.setStr("accessTokenXcx_"+configurationFiles.getWxXcxAppId(),tokenResultMap.get("accessToken"),expiresInMinute);
        }
        return  redisService.getStr("accessToken_"+configurationFiles.getWxXcxAppId());
    }

    /**
     * 模板推送
     * @param accessToken 微信唯一凭证
     * @param data 推送消息结构
     * @return TemplateMsgResult
     */
    public TemplateMsgResult sendTemplate(String accessToken, String data){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("access_token", accessToken);
        log.info("data {} ",data);
        String result = HttpReqUtil.HttpsDefaultExecute("POST",WxUtil.URL,params, data,"UTF-8");
        TemplateMsgResult templateMsgResult = JsonUtils.fromJsonString(result, TemplateMsgResult.class);
        log.info("sendTemplateresult {} ",result);
        return templateMsgResult;
    }
}
