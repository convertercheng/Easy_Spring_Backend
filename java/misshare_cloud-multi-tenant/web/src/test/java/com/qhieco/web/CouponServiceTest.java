package com.qhieco.web;

import com.qhieco.commonentity.Tag;
import com.qhieco.commonrepo.UserTagBRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.webmapper.CouponMapper;
import com.qhieco.webmapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/26 11:09
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class CouponServiceTest {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserTagBRepository userTagBRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getTagByUserIdTest() {
        CouponRequest couponRequest = new CouponRequest();
        ArrayList a = new ArrayList<Integer>();
        a.add(53);
        couponRequest.setTagList(a);
        List<Tag> list = couponMapper.getTagByUserId(couponRequest.getTagList());
        log.info("标签列表信息：" + list);
        Set<Integer> userList = new HashSet<>();
        for (Tag tag : list) {
            List<Integer> idList;
            if (Status.TagType.MANUAL.getInt().equals(tag.getType())) {
                idList = userTagBRepository.findByTagId(tag.getId());
            } else {
                idList = userMapper.findTagUserId(tag);
            }
            log.info("发放优惠券的用户列表：" + userList);
            userList.addAll(idList);
        }
        log.info(" ---------" + list);
    }
}
