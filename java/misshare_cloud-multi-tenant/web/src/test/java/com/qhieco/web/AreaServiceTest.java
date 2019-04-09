package com.qhieco.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.qhieco.commonentity.Area;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AreaRequest;
import com.qhieco.response.Resp;
import com.qhieco.webservice.impl.AreaServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/12 下午2:57
 *          <p>
 *          类说明：
 *          AreaService测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AreaServiceTest {
    @Autowired
    private AreaServiceImpl areaService;

    @Test
    public void save() throws Exception {
        Area area = new Area();
        area.setName("area");
        area.setLevel(2);
        area.setParentId(1);
        area.setCreateTime(System.currentTimeMillis());
        log.info("getCreateTime: {}", area.getCreateTime());
        area.setModifyTime(System.currentTimeMillis());
        area.setState(Status.Common.VALID.getInt());
        Resp resp = areaService.save(area);
        log.info("getError_code: {}", resp.getError_code());
        log.info("getData: {}", resp.getData());
    }

    @Test
    public void children() throws Exception {
        AreaRequest areaRequest = new AreaRequest();
        areaRequest.setSEcho(1);
        areaRequest.setLength(10);
        areaRequest.setStart(0);
        areaRequest.setParentId(0);
        log.info("pageable: {}", areaService.children(areaRequest));
    }
}
