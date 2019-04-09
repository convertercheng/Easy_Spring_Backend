package com.qhieco.web;

import com.qhieco.commonentity.Role;
import com.qhieco.commonrepo.RoleRepository;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.request.api.BankCardUnbindRequest;
import com.qhieco.request.web.RoleRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.webservice.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-6 下午3:13
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class roleServiceTest {

    @Autowired
    RoleServiceImpl roleService;

    @Autowired
    RoleRepository roleRepository;

//    @Test
    public void test(){
        Resp<List<Role>> resp = roleService.findAll();
        List<Role> dataList = resp.getData();
        for (Role role:dataList
             ) {
            log.info(role.getName());
        }
    }

//    @Test
    public void testParam(){
        BankCardUnbindRequest bankCardUnbindRequest = new BankCardUnbindRequest();
        Resp resp = ParamCheck.check(bankCardUnbindRequest, "dasf","user_id","token","timestamp");
        log.info(Integer.toString(resp.getError_code()));
        log.info(resp.getError_message());
    }

//    @Test
    public void testExcelUtil(){
        Resp<List<Role>> resp = roleService.findAll();
        List<Role> dataList = resp.getData();
        System.out.println(ExcelUtil.dataToMap(dataList, Role.class));
    }

//    @Test
    public void testPage(){
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setSEcho(0);
        roleRequest.setStart(0);
        roleRequest.setLength(10);
        Resp checkResp = ParamCheck.check(roleRequest, "sEcho", "start", "length");
//        System.out.println(checkResp);
        Resp roleDataResp = roleService.all(roleRequest);
        System.out.println(roleDataResp.getData());
    }

    @Test
    public void testProperty(){
        Role role = new Role();
        role.setId(1);
        role.setName("管理员");
        System.out.println(PropertyCheck.uniqueCheck("name",role,roleRepository));
    }

    @Autowired
    UserWebRespository userWebRespository;

    @Test
    public void testUser(){
        System.out.println(userWebRespository.findOne(2));
    }
}
