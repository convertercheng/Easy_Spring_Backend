package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.ParklotDistrict;
import com.qhieco.commonrepo.ParklotDistrictRepository;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotDistrictRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.ParklotDistrictService;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.impl.wx.WxService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 18-7-06 上午11:01
 * <p>
 * 类说明：停车场区域业务层
 * ${description}
 */
@Service
@Slf4j
public class ParklotDistrictServiceImpl implements ParklotDistrictService {

    @Autowired
    private ParklotDistrictRepository parklotDistrictRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    private QueryFunction<ParklotDistrict, ParklotDistrictRequest> queryFunction;

    @Autowired
    private WxService wxService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(parklotDistrictRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    /**
     * 初始化停车场名称
     *
     * @param data
     * @return
     */
    private ParklotDistrict transientProperty(ParklotDistrict data) {
        if (data.getParklotId() != null) {
            Parklot parklot = parklotRepository.findOne(data.getParklotId());
            data.setParkLotName(parklot.getName());
            String typeStr = "";
            if (Status.ParkingInner.INNERONE.getValue().equals(parklot.getInnershare())) {
                typeStr = parklot.getTypeStr() + parklot.getInnershareStr();
            } else {
                typeStr = parklot.getTypeStr();
            }
            data.setParkLotTypeStr(typeStr);
        }
        return data;
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp query(ParklotDistrictRequest parklotDistrictRequest) throws Exception {
        return queryFunction.query(parklotDistrictRequest, where(parklotDistrictRequest));
    }

    @Override
    public Resp saveUpdate(ParklotDistrict parklotDistrict) throws Exception {
        Integer id = parklotDistrict.getId();

        /**
         * 编辑区域信息否则就是新增区域信息
         */
        if (id != null) {
            ParklotDistrict parklotDistrict1 = parklotDistrictRepository.findParklotDistrictByDistrictByName
                    (parklotDistrict.getDistrictName(), parklotDistrict.getParklotId(), Status.Common.VALID.getInt(), id);
            if (parklotDistrict1 != null) {
                throw new QhieWebException(Status.WebErr.DISTRICT_NAME_EXISTS);
            }
        } else {
            ParklotDistrict parklotDistrict1 = parklotDistrictRepository.findParklotDistrictByDistrictNameAndParklotIdAndState
                    (parklotDistrict.getDistrictName(), parklotDistrict.getParklotId(), Status.Common.VALID.getInt());
            if (parklotDistrict1 != null) {
                throw new QhieWebException(Status.WebErr.DISTRICT_NAME_EXISTS);
            }
        }
        parklotDistrict.setUpdateTime(System.currentTimeMillis());
        parklotDistrict.setState(Status.Common.VALID.getInt());
        return RespUtil.successResp(parklotDistrictRepository.save(parklotDistrict));
    }

    @Override
    @EnableTenantFilter
    public Resp getParklotDistrictInfo(Integer id) throws Exception {
        return RespUtil.successResp(transientProperty(parklotDistrictRepository.findByIdAndState(id, Status.Common.VALID.getInt())));
    }

    @Override
    @EnableTenantFilter
    public Resp excel(ParklotDistrictRequest parklotDistrictRequest, OutputStream outputStream) throws Exception {
        return queryFunction.excel(where(parklotDistrictRequest), ParklotDistrict.class, outputStream);
    }

    /**
     * where条件
     * @param request
     * @return
     */
    private Specification<ParklotDistrict> where(ParklotDistrictRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            page.equal("state", Status.Common.VALID.getInt());
            String districtName = request.getDistrictName();
            if (StringUtils.isNotEmpty(districtName)) {
                page.like("districtName", districtName);
            }
            String parklotName = request.getParklotName();
            if (StringUtils.isNotEmpty(parklotName)) {
                val idList = parklotRepository.findIdsByNameLike("%" + parklotName + "%");
                page.in("parklotId", idList);
            }
            return page.pridect();
        };
    }

    @Override
    public Resp getParklotDistrictByParklotList(Integer parklotId) throws Exception {
        return RespUtil.successResp(parklotDistrictRepository.getDistinctByParklotIdAndState(parklotId,Status.Common.VALID.getInt()));
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
                    params.put("path", "/pages/index/index?parklotDistrictId=" + ids);
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
                    ParklotDistrict parklotDistrict=parklotDistrictRepository.findByIdAndState(Integer.parseInt(ids),Status.Common.VALID.getInt());
                    parklotDistrict=transientProperty(parklotDistrict);
                    String name = parklotDistrict.getParkLotName()+"_"+parklotDistrict.getDistrictName()+ ".png";
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
}
