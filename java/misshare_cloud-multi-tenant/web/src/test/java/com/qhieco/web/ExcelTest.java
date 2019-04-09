package com.qhieco.web;
import com.qhieco.constant.Status.OrderType;
import com.qhieco.util.ExcelUtil;
import com.qhieco.webservice.impl.RoleServiceImpl;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExcelTest {

    @Autowired
    RoleServiceImpl roleService;

    @Test
    public void test() throws IOException {
        BigDecimal decimal = new BigDecimal(1);
        log.info(decimal.getClass().getSimpleName());
    }
}
