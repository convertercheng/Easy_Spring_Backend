package com.qhieco.webservice.impl;


import com.qhieco.commonentity.Update;
import com.qhieco.commonrepo.UpdateRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.UpdateRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    UpdateRepository updateRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp saveOrUpdate(UpdateRequest updateRequest){
        String vcode = updateRequest.getVcode();
        String updateinfo = updateRequest.getUpdateinfo();
        String forceupdate = updateRequest.getForceupdate();
        Integer type = updateRequest.getType();
        if(StringUtils.isEmpty(forceupdate)) {
            List<Update> updates = updateRepository.findForceUpdateList(type);
            if(null != updates && 0 != updates.size()) {
                forceupdate = updates.get(updates.size() - 1).getForceupdate();
            }
        }
        Update update = new Update(vcode,updateinfo,forceupdate,type,System.currentTimeMillis(), Status.Common.VALID.getInt());
        updateRepository.save(update);
        return RespUtil.successResp();
    }


}
