package com.qhieco.webservice.impl.wx;


import com.qhieco.config.ConfigurationFiles;
import com.qhieco.util.HttpReqUtil;
import com.qhieco.util.JsonUtils;
import com.qhieco.util.TemplateMsgResult;
import com.qhieco.util.WxUtil;
import com.qhieco.webservice.impl.redis.RedisService;
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
public class WxService implements CommandLineRunner{
    @Autowired
    private RedisService redisService;

    @Autowired
    private ConfigurationFiles configurationFiles;
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
        return  redisService.getStr("accessTokenXcx_"+configurationFiles.getWxXcxAppId());
    }

    @Override
    public void run(String... strings) throws Exception {
        Map<String,String> tokenXcxResultMap= WxUtil.getAccessToken(configurationFiles.getWxXcxAppId(),configurationFiles.getWxXcxSecret());
        Long expiresInXcxMinute=Long.valueOf(tokenXcxResultMap.get("expiresIn"));
        if(tokenXcxResultMap==null){
            log.error("get tokenXcxResultMap wechat error info....");
            return;
        }
        redisService.setStr("accessTokenXcx_"+configurationFiles.getWxXcxAppId(),tokenXcxResultMap.get("accessToken"), expiresInXcxMinute);
    }
}
