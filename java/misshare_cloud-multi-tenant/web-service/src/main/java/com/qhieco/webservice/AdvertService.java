package com.qhieco.webservice;

import com.qhieco.commonentity.Advert;
import com.qhieco.response.data.web.AdvertData;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 10:51
 * <p>
 * 类说明：
 * ${说明}
 */
public interface AdvertService {

    public Advert saveOrUpdate(String id, int phoneType, int countdown, int jumpable, String href, Integer fileId, String originalName, String filePath);

    public AdvertData queryByPhoneType(int phoneType);

    public String queryPathById(Integer fileId);

    public void deleteById(int id, int fileId);
}
