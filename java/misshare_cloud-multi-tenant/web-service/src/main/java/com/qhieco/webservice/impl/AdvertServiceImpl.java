package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Advert;
import com.qhieco.commonentity.File;
import com.qhieco.commonrepo.AdvertRepository;
import com.qhieco.commonrepo.FileRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Status;
import com.qhieco.response.data.web.AdvertData;
import com.qhieco.webmapper.AdverMapper;
import com.qhieco.webmapper.FileMapper;
import com.qhieco.webservice.AdvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 10:52
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private AdverMapper adverMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Advert saveOrUpdate(String id, int phoneType, int countdown, int jumpable, String href, Integer fileId, String originalName, String filePath) {
        // 上传了图片
        if (!StringUtils.isEmpty(filePath)) {
            // 上传了新图片，则把之前的图片信息删除
            if (fileId != null) {
                fileMapper.deleteById(fileId);
            }
            File file = new File();
            file.setName(originalName);
            file.setPath(filePath);
            file.setIntro(originalName);
            file.setState(Status.Common.VALID.getInt());
            file.setCreateTime(System.currentTimeMillis());
            file = fileRepository.save(file);
            fileId = file.getId();
            log.info("保存广告页图片信息 file = " + file);
        }

//        Integer advertId = adverMapper.queryIdByPhoneType(phoneType);
//        Advert advert = new Advert();
//        if (!StringUtils.isEmpty(id) || advertId != null) {
//            advert.setId(Integer.valueOf(id));
//        }
        Advert advert = advertRepository.findByPhoneType(phoneType);
        if (advert == null ) {
            advert = new Advert();
        }
        advert.setPhoneType(phoneType);
        advert.setCountdown(countdown);
        advert.setJumpable(jumpable);
        advert.setHref(href);
        advert.setCreateTime(System.currentTimeMillis());
        advert.setState(Status.Common.VALID.getInt());
        advert.setFileId(fileId);
        log.info(" 保存广告页数据： advert = " + advert);
        advertRepository.save(advert);
        return advert;
    }

    @Override
    public AdvertData queryByPhoneType(int phoneType) {
        AdvertData advertData = adverMapper.queryByPhoneType(phoneType);
        if (advertData != null && !StringUtils.isEmpty(advertData.getFilePath())) {
            advertData.setFilePath(configurationFiles.getPicPath() + advertData.getFilePath());
        }
        return advertData;
    }

    @Override
    public String queryPathById(Integer fileId) {
        return fileMapper.queryPathById(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id, int fileId) {
        String originalFilePath = fileMapper.queryPathById(fileId);
        new java.io.File(configurationFiles.getWebUploadPath() + java.io.File.separator + originalFilePath).delete();

        fileMapper.deleteById(fileId);
        adverMapper.deleteById(id);
    }
}
