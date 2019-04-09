package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.LogLock;
import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.GatewayRepository;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.IdRequest;
import com.qhieco.request.web.LockRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.LockData;
import com.qhieco.util.*;
import com.qhieco.webmapper.LockMapper;
import com.qhieco.webservice.LockService;
import com.qhieco.webservice.impl.wx.WxService;
import com.qhieco.webservice.utils.QueryFunction;
import io.netty.util.Constant;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-16 下午4:27
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class LockServiceImpl extends AbstractIotServiceImpl<Lock, LockRequest> implements LockService {

    private static final String BATTERY_RE = "[^\\d]{1,2}([\\d\\.]+)";
    private static final Integer GT = 1;
    private static final Integer LT = 0;
    private static final String GT_CHAR = ">";
    private static final String LT_CHAR = "<";
    private static final String STATE = "state";
    @Autowired
    LockRepository lockRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    ParklotAdminRepository parklotAdminRepository;

    @Autowired
    LogLockRepository logLockRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    private LockMapper lockMapper;

    private QueryFunction<LogLock, IdRequest> queryFunction;

    @Autowired
    private WxService wxService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Override
    public Resp saveLock(Lock lock) {
        // 判断车位锁蓝牙名称或者MAC地址是否有重复
        if (lockMapper.queryNameAdMacDuplicate(lock.getBtName(), lock.getMac(), lock.getId()) > 0) {
            log.error("新增或者修改车锁的蓝牙名称或者Mac地址重复， 保存失败，lock = " + lock);
            return RespUtil.errorResp(Status.WebErr.LOCK_BTNAME_BTMAC_DULICATE.getCode(), Status.WebErr.LOCK_BTNAME_BTMAC_DULICATE.getMsg());
        }
        if (lock == null) {
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg());
        }
        Integer gatewayId = lock.getGatewayId();
        if (lock.getId() != null) {
            BeanUtil.converJavaBean(getDao().findOne(lock.getId()), lock);
        }
        lock.setGatewayId(gatewayId);
        if (lock.getUpdateTime() == null) {
            lock.setUpdateTime(System.currentTimeMillis());
        }
        if (lock.getState() == null) {
            lock.setState(Status.Common.VALID.getInt());
        }
        Lock iot = lockRepository.save(lock);
        if (iot == null) {
            return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
        }
        return RespUtil.successResp();
    }

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(logLockRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private LogLock transientProperty(LogLock data) {
        val newData = new LogLock();
        if (null != data.getLockId()) {
            Lock lock = lockRepository.findOne(data.getLockId());
            data.setLockNumber(lock.getSerialNumber());
            data.setLockMac(lock.getMac());
            if (null != lock.getGatewayId()) {
                Gateway gateway = gatewayRepository.findOne(lock.getGatewayId());
                data.setIdentifier(gateway.getIdentifier());
            }
        }
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }

    @Override
    public IotRepository<Lock> getDao() {
        return lockRepository;
    }

    @Override
    protected Specification<Lock> where(LockRequest request) {
        handleBattry(request);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (request.getParklotName() != null && !"".equals(request.getParklotName())) {
                List<Integer> parklotIds = parklocRepository.findByParklotName("%" + request.getParklotName() + "%");
                if (parklotIds.size() > 0) {
                    predicates.add(root.<Integer>get("parklocId").in(parklotIds));
                } else {
                    predicates.add(root.<Integer>get("parklocId").in(-1));
                }
            }
            if (request.getType() != null) {
                PageableUtil.equal(root, cb, predicates, "type", request.getType());
            }
            PageableUtil.like(root, cb, predicates, "serialNumber", request.getSerialNumber());
            PageableUtil.equal(root, cb, predicates, "state", request.getState());
            if (request.getBatteryNum() != null) {
                if (GT.equals(request.getBatteryType())) {
                    PageableUtil.between(root, cb, predicates, "battery", request.getBatteryNum(), 100);
                }
                if (LT.equals(request.getBatteryType())) {
                    PageableUtil.between(root, cb, predicates, "battery", 0.00, request.getBatteryNum());
                }
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    private void handleBattry(LockRequest request) {
        String batteryQuery = request.getBattery();
        if (batteryQuery == null) {
            return;
        }
        if (batteryQuery.contains(GT_CHAR)) {
            Pattern r = Pattern.compile(BATTERY_RE);
            Matcher m = r.matcher(batteryQuery);
            if (m.find()) {
                System.out.println(Double.parseDouble(m.group(1)));
                request.setBatteryNum(Double.parseDouble(m.group(1)));
            }
            request.setBatteryType(GT);
        }
        if (batteryQuery.contains(LT_CHAR)) {
            Pattern r = Pattern.compile(BATTERY_RE);
            Matcher m = r.matcher(batteryQuery);
            if (m.find()) {
                System.out.println(Double.parseDouble(m.group(1)));
                request.setBatteryNum(Double.parseDouble(m.group(1)));
            }
            request.setBatteryType(LT);
        }
    }


    @Override
    protected Specification<Lock> unbindWhere(LockRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            PageableUtil.isNull(root, cb, predicates, "parklocId");
            PageableUtil.like(root, cb, predicates, "name", request.getName());
            PageableUtil.like(root, cb, predicates, "serialNumber", request.getSerialNumber());
//            PageableUtil.equal(root, cb, predicates, "state", request.getState());
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    @Override
    protected void transientProperty(Lock data) {
        if (data.getParklocId() != null) {
            val parkloc = parklocRepository.findOne(data.getParklocId());
            if (parkloc == null) {
                return;
            }
            data.setParklocNumber(parkloc.getNumber());
            val parklot = parklotRepository.findByParkloc(data.getParklocId());
            if (parklot == null) {
                return;
            }
            data.setParklotName(parklot.getName());
            data.setParklotTypeStr(Status.ParklotType.find(parklot.getType()));
            data.setParklotType(parklot.getType());
            data.setInnershare(parklot.getInnershare());
            data.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
            data.setTypeStr(Status.Lock.find(data.getType()));
        }

    }

    @Override
    protected void reverseLookup(Map<String, Object> map) {
        if (map.get(STATE) != null) {
            map.put("stateStr", Status.Common.find((Integer) map.get("state")));
        }
    }

    @Override
    @EnableTenantFilter
    public Resp query(LockRequest request) {
        val resp = super.query(request);
        if (!resp.getError_code().equals(Status.WebErr.SUCCESS.getCode())) {
            return resp;
        }
        val data = resp.getData();
        val lockList = data.getDataList();
        Integer invalidNum = lockRepository.countInvalid(Status.Common.INVALID.getInt());
        Integer noPowerNum = lockRepository.countNoPower(0.3);
        LockData lockData = new LockData();
        BeanUtil.converJavaBean(resp.getData(), lockData);
        lockData.setInvalidNum(invalidNum);
        lockData.setNoPowerNum(noPowerNum);
        return RespUtil.successResp(lockData);
    }


    @Override
    public Resp logPageable(IdRequest request) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return queryFunction.queryOrder(request, logLockWhere(request), sort);
    }

    @Override
    public void downloadAllQr(HttpServletRequest request, HttpServletResponse response, String macIds) throws Exception {
        String str[]=macIds.split(",");
        String token = wxService.replaceXcxAccessToken();
        List<String> files=new ArrayList<>();
        int taskSize = str.length;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        //并发任务计数器，可以阻塞等待所有任务完成
        CountDownLatch countDownLatch = new CountDownLatch(taskSize);

        for(String ids:str){
            // 执行任务并获取Future对象
            Future f = pool.submit(()->{
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("path", "/pages/index/index?macId=" + ids);
                    params.put("width", 800);
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=" + token);  // 接口
                    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
                    String body = net.sf.json.JSONObject.fromObject(params).toString();           //必须是json模式的 post
                    StringEntity entity;
                    entity = new StringEntity(body);
                    entity.setContentType("image/png");
                    httpPost.setEntity(entity);
                    HttpResponse responses;
                    responses = httpClient.execute(httpPost);
                    InputStream inputStream = responses.getEntity().getContent();
                    String name = ids + ".png";
                    File targetFile = new File(configurationFiles.getWebUploadPath() + "wx/");
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(configurationFiles.getWebUploadPath() + "wx/" + name);
                    byte[] buffer = new byte[8192];
                    int bytesRead = 0;
                    while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    //ArrayList线程不安全，加锁同步
                    synchronized(files) {
                        files.add(configurationFiles.getPicPath() + "wx/" + name + "," + name);
                    }
                    out.flush();
                    out.close();
                }catch (Exception e){
                    log.error(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        // 关闭线程池
        pool.shutdown();
        String downloadFilename = System.currentTimeMillis()+".zip";//文件的名称
        downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
        response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
        response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        for (int i=0;i<files.size();i++) {
            String[] strs=files.get(i).split(",");
            URL url = new URL(strs[0]);
            zos.putNextEntry(new ZipEntry(strs[1]));
            //FileInputStream fis = new FileInputStream(new File(files[i]));
            InputStream fis = url.openConnection().getInputStream();
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
            }
            fis.close();
        }
        zos.flush();
        zos.close();
    }


    private Specification<LogLock> logLockWhere(IdRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            if (null != request.getId() && Constants.MIN_NON_NEGATIVE_INTEGER != request.getId()) {
                page.equal("lockId", request.getId());
            }
            return page.pridect();
        };
    }



}
