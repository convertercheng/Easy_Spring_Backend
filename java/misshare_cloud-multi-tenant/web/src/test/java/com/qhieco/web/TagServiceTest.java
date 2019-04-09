package com.qhieco.web;

import com.qhieco.request.web.ManualTagSaveRequest;
import com.qhieco.request.web.TagUserRequest;
import com.qhieco.webmapper.TagMapper;
import com.qhieco.webservice.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/15 16:42
 * <p>
 * 类说明：
 * ${说明}
 */
@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagMapper tagMapper;

    @Test
    public void saveOrUpdateManualTagTest() {
        ManualTagSaveRequest request = new ManualTagSaveRequest();
        request.setTagId(46);
        request.setComment("新标签改名");
        request.setTagName("tag1该欧美");
        List<Integer> users = new ArrayList<>();
        users.add(5);
        users.add(6);
        request.setUserIds(users);
        log.info(" ---------------- " + tagService.saveOrUpdateManualTag(request));
    }

    @Test
    public void queryUserListTest() {
        TagUserRequest request = new TagUserRequest();
        request.setSEcho(1);
        request.setStart(0);
        request.setLength(10);
        request.setTagId(129);
//        request.setPhone("137");
//        request.setUserType(0);
        log.info(" ----------------- " + tagService.queryUserList(request));
    }

    @Test
    public void queryBindUsersByTagIdTest(){
        log.info(" ----------------- " + tagMapper.queryBindUserInfoByTagId(53));
    }
}
