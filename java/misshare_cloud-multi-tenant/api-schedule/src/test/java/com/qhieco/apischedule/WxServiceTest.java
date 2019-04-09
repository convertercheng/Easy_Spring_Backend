package com.qhieco.apischedule;

import com.qhieco.config.ConfigurationFiles;
import com.qhieco.util.HttpUtil;
import com.qhieco.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 14:02
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class WxServiceTest {

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Test
    public void getAccessToken() {
        Map<String, String> tokenResultMap = WxUtil.getAccessToken(configurationFiles.getWxMpAppId(), configurationFiles.getWxMpSecret());
        log.info(" tokenResultMap = " + tokenResultMap);
        getFocusUsers("10_eNOT_zTntNu-bRo8vmScr2Mrg0cj4th3KgVGvCRtl4QXdGIJ4degdWtsdXnmPSqRgbHbkRIFm8WiA_YlzoTcNzpWG7F_kVA-e9egqxgAkUQ");
    }

    private void getFocusUsers(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
        String jsonObject = HttpUtil.doGet(url);
        JSONObject focusUsers = JSONObject.fromObject(jsonObject);
        log.info("获取公众号关注用户列表结果：" + focusUsers);
    }
}
