package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Business;
import com.qhieco.commonentity.Coupon;
import com.qhieco.commonentity.Tag;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonrepo.BusinessRepository;
import com.qhieco.commonrepo.CouponRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.commonrepo.UserTagBRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.CouponData;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.CouponMapper;
import com.qhieco.webmapper.UserMapper;
import com.qhieco.webservice.CouponService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 20:49
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    UserTagBRepository userTagBRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserMapper userMapper;

    private QueryFunction<Coupon, CouponRequest> queryFunction;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(couponRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private Coupon transientProperty(Coupon data) {
        val newData = new Coupon();
        if (data.getBusinessId() != null) {
            Business business = businessRepository.findOne(data.getBusinessId());
            if (business != null) {
                data.setBusinessName(business.getBusinessName());
            }
        }
        if (data.getMobileUserId() != null) {
            UserMobile userMobile = userMobileRepository.findOne(data.getMobileUserId());
            if (userMobile != null) {
                data.setUserPhone(userMobile.getPhone());
            }
        }
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }


    private static Set<String> couponCodeSet = new HashSet<String>();

    private static final char[] r = new char[]{'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'P', 'A', 'B', 'C', 'D', 'F', 'G', 'H', 'J',
            'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final int binLen = r.length;

    private static final int s = 8;


    @Override
    public Resp userCouponQuery(CouponRequest couponRequest) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return queryFunction.queryOrder(couponRequest, userWhere(couponRequest), sort);
    }

    @Override
    public Resp userCouponExecl(CouponRequest couponRequest, OutputStream outputStream, Class cl) throws IOException {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        List<Coupon> list=couponRepository.findAll(userWhere(couponRequest),sort);
        list.forEach(this::transientProperty);
        List<CouponData> couponDataList=new ArrayList<CouponData>();
        for(Coupon coupon:list){
            CouponData couponData=new CouponData();
            couponData.setUsedMoney(coupon.getUsedMoney());
            couponData.setBeginTime(coupon.getBeginTime());
            couponData.setCouponCode(coupon.getCouponCode());
            couponData.setCouponLimit(coupon.getCouponLimit());
            couponData.setCouponPassword(coupon.getCouponPassword());
            couponData.setCouponType(coupon.getCouponType());
            couponData.setCreateTime(coupon.getCreateTime());
            couponData.setEndTime(coupon.getEndTime());
            couponData.setUsedTime(coupon.getUsedTime());
            couponData.setUserPhone(coupon.getUserPhone());
            couponData.setState(coupon.getState());
            String stateStr= Status.Coupon.find(coupon.getState());
            String couponTypeStr=Status.Coupon.find(coupon.getCouponType());
            couponData.setStateStr(stateStr);
            couponData.setCouponTypeStr(couponTypeStr);
            couponDataList.add(couponData);
        }
        ExcelUtil<CouponData> excelUtil = new ExcelUtil<>(outputStream,CouponData.class);
        excelUtil.write(couponDataList);
        return queryFunction.excel(userWhere(couponRequest), cl, outputStream);
    }

    private Specification<Coupon> userWhere(CouponRequest request) {
        return (root, query, cb) -> {
            System.out.println(request);
            Long now = System.currentTimeMillis();
            val page = new PageableUtil(root, query, cb);
            page.notequal("mobileUserId");
            page.isNull("businessId");
            if (StringUtils.isNotEmpty(request.getUserPhone())) {
                val ids = userMobileRepository.findByPhoneIds("%" + request.getUserPhone() + "%");
                page.in("mobileUserId", ids);
            }
            if (StringUtils.isNotEmpty(request.getCouponCode())) {
                page.like("couponCode", request.getCouponCode());
            }
            if (request.getMinCouponLimit() != null) {
                page.greaterThanEqual(cb, "couponLimit", request.getMinCouponLimit());
            }
            if (request.getMaxCouponLimit() != null) {
                page.lessThanOrEqual(cb, "couponLimit", request.getMaxCouponLimit());
            }
            if (request.getMinUsedMoney() != null) {
                page.greaterThanEqual(cb, "usedMoney", request.getMinUsedMoney());
            }
            if (request.getMaxUsedMoney() != null) {
                page.lessThanOrEqual(cb, "usedMoney", request.getMaxUsedMoney());
            }
            if (request.getState() != null) {
                page.equal("state", request.getState());
            }
            /*//如果查询不为已过期优惠卷
            if (request.getState()!=null && !Status.Coupon.COUPON_EXPIRE.getInt().equals(request.getState())) {
                page.lessThanOrEqual(cb, "beginTime",now);
                page.greaterThanEqual(cb, "endTime",now);
            }
            //如果查询为已过期优惠卷
            if (Status.Coupon.COUPON_EXPIRE.getInt().equals(request.getState())) {
                page.notBetween(cb, "endTime","beginTime",now);
            }*/
            if (request.getStartCreateTime() != null && request.getEndCreateTime() != null) {
                page.between("createTime", request.getStartCreateTime(), request.getEndCreateTime());
            }
            if (request.getStartUsedTime() != null && request.getEndUsedTime() != null) {
                page.between("usedTime", request.getStartUsedTime(), request.getEndUsedTime());
            }
            if (request.getBeginTime() != null) {
                page.greaterThanEqual(cb, "beginTime", request.getBeginTime());
            }
            if (request.getEndTime() != null) {
                page.lessThanOrEqual(cb, "endTime", request.getEndTime());
            }
            return page.pridect();
        };
    }

    @Override
    public Resp<AbstractPaged<Coupon>> query(CouponRequest couponRequest) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return queryFunction.queryOrder(couponRequest, where(couponRequest), sort);
    }

    @Override
    public Resp execl(CouponRequest couponRequest, OutputStream outputStream, Class cl) throws IOException {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        List<Coupon> list=couponRepository.findAll(where(couponRequest),sort);
        if(list.size()==Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(int i=0;i<list.size();i++){
           String stateStr= Status.Coupon.find(list.get(i).getState());
           String couponTypeStr=Status.Coupon.find(list.get(i).getCouponType());
            list.get(i).setStateStr(stateStr);
            list.get(i).setCouponTypeStr(couponTypeStr);
            list.set(i,list.get(i));
        }
        list.forEach(this::transientProperty);
        ExcelUtil<Coupon> excelUtil = new ExcelUtil<>(outputStream,Coupon.class);
        excelUtil.write(list);
        return RespUtil.successResp();
    }

    private Specification<Coupon> where(CouponRequest request) {
        return (root, query, cb) -> {
            Long now = System.currentTimeMillis();
            val page = new PageableUtil(root, query, cb);
            page.notequal("businessId");
            if (StringUtils.isNotEmpty(request.getBusinessName())) {
                val ids = businessRepository.findBusinessByNameList(request.getBusinessName());
                page.in("businessId", ids);
            }
            if (StringUtils.isNotEmpty(request.getCouponCode())) {
                page.like("couponCode", request.getCouponCode());
            }
            if (request.getCouponType() != null) {
                page.equal("couponType", request.getCouponType());
            }
            if (request.getState() != null) {
                page.equal("state", request.getState());
            }
            /*//如果查询不为已过期优惠卷
            if (!Status.Coupon.COUPON_EXPIRE.getInt().equals(request.getState())) {
                page.lessThanOrEqual(cb, "beginTime",now);
                page.greaterThanEqual(cb, "endTime",now);
            }
            //如果查询为已过期优惠卷
            if (Status.Coupon.COUPON_EXPIRE.getInt().equals(request.getState())) {
                page.notBetween(cb, "endTime","beginTime",now);
            }*/
            if (request.getStartCreateTime() != null && request.getEndCreateTime() != null) {
                page.between("createTime", request.getStartCreateTime(), request.getEndCreateTime());
            }
            return page.pridect();
        };
    }

    @Transactional
    @Override
    public Resp save(CouponRequest couponRequest) {
        Resp resp = null;
        if (Constants.ONE_CAPACITY.equals(couponRequest.getOperationType())) {
            if (couponRequest.getTagList().size() > Constants.EMPTY_CAPACITY) {
                List<Tag> list=couponMapper.getTagByUserId(couponRequest.getTagList());
                log.info("标签列表信息：" + list);
                Set<Integer> userList = new HashSet<>();
                for(Tag tag:list){
                    List<Integer> idList;
                    if(Status.TagType.MANUAL.getInt().equals(tag.getType())) {
                        idList = userTagBRepository.findByTagId(tag.getId());
                    }else{
                        idList = userMapper.findTagUserId(tag);
                    }
                    userList.addAll(idList);
                }
                log.info("发放优惠券的用户列表：" + userList);
                couponRequest.setUserIdList(new ArrayList<>(userList));
            }
            resp = saveCoupn(couponRequest.getUserIdList(), couponRequest);
        } else {
            resp = saveCoupn(couponRequest);
        }
        return resp;
    }

    private Resp saveCoupn(List<Integer> userIdList, CouponRequest couponRequest) {
        if (couponRequest.getCouponNum() > 10) {
            couponRequest.setCouponNum(10);
        }
        List<Coupon> list = new ArrayList<Coupon>();
        for (int i = 0; i < couponRequest.getUserIdList().size(); i++) {
            Set<String> codeList = code(couponRequest.getCouponNum());
            for (String code : codeList) {
                Coupon coupon = new Coupon();
                coupon.setMobileUserId(couponRequest.getUserIdList().get(i));
                coupon.setCouponCode(code);
                coupon.setState(Status.Coupon.COUPON_CONVERTIBILITY.getInt());
                generatingCoupons(coupon, couponRequest);
                list.add(coupon);
            }
        }
        batchInsert(list);
        return RespUtil.successResp();
    }

    private Resp saveCoupn(CouponRequest couponRequest) {
        List<Coupon> list = new ArrayList<Coupon>();
        Set<String> codeList = code(couponRequest.getCouponNum());
        for (String code : codeList) {
            Coupon coupon = new Coupon();
            generatingCoupons(coupon, couponRequest);
            coupon.setBusinessId(couponRequest.getBusinessId());
            coupon.setMobileUserId(null);
            coupon.setCouponCode(code);
            coupon.setState(Status.Coupon.UNCLAIMED.getInt());
            list.add(coupon);
        }
        batchInsert(list);
        return RespUtil.successResp();
    }

    private void generatingCoupons(Coupon coupon, CouponRequest couponRequest) {
        Random random = new Random();
        int password = random.nextInt(999999) % (999999 - 100000 + 1) + 100000;
        coupon.setCouponPassword(String.valueOf(password));
        coupon.setUsedMoney(new BigDecimal(0));
        coupon.setActivityId(null);
        coupon.setCouponType(Status.Coupon.COUPON_CONEN_TYPE_ONE.getInt());
        coupon.setBeginTime(couponRequest.getBeginTime());
        coupon.setEndTime(couponRequest.getEndTime());
        coupon.setCouponLimit(couponRequest.getCouponLimit());
        coupon.setCreateTime(System.currentTimeMillis());
    }

    /**
     * 生成优惠券
     *
     * @param number
     * @return
     */
    private Set<String> code(Integer number) {
        Set<String> codeSet = new HashSet<String>();
        for (int i = 0; i < number; i++) {
            Random random = new Random();
            if (Constants.EMPTY_CAPACITY == couponCodeSet.size()) {
                List<Coupon> listAll = couponRepository.findAll();
                for (Coupon coupon1 : listAll) {
                    couponCodeSet.add(coupon1.getCouponCode());
                }
            }
            int setSize = couponCodeSet.size();
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < s; j++) {
                sb.append(r[random.nextInt(binLen)]);
            }
            String code = sb.toString();
            couponCodeSet.add(code);
            //如果在Set中有重复
            if (couponCodeSet.size() == setSize) {
                i--;
            } else {
                boolean isDigit = false;//是否包含数字
                boolean isLetter = false;//否包含字母
                //循环遍历字符串
                for (int p = 0; p < code.length(); p++) {
                    if (Character.isDigit(code.charAt(p))) {
                        isDigit = true;
                    }
                    if (Character.isLetter(code.charAt(p))) {
                        isLetter = true;
                    }
                }
                //如果包含数字和字母
                if (isDigit && isLetter) {
                    codeSet.add(code);
                } else {
                    i--;
                }

            }

        }

        return codeSet;
    }

    @Transactional(readOnly = false)
    public void batchInsert(List<Coupon> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Coupon dd = list.get(i);
            em.persist(dd);
            if (i % 1000 == 0 || i == (size - 1)) { // 每1000条数据执行一次，或者最后不足1000条时执行
                em.flush();
                em.clear();
            }
        }
    }

}
