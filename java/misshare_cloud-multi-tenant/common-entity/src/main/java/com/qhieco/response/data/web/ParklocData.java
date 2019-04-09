package com.qhieco.response.data.web;

import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.constant.Status;
import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-27 下午2:33
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParklocData extends Parkloc{


    private String parklotName;

    private Integer parklotType;

    private String lockNumber;

    private String userName;

    private String userPhone;

    private String parklotTypeStr;

    private Integer innershare;

    private String innershareStr;

    private String parklotDistrictName;

    public void setParklot(Parklot parklot){
        if (parklot!=null){
            parklotName = parklot.getName();
            parklotType = parklot.getType();
        }
    }

    public void setUser(UserMobile user){
        if (user!=null){
            userName = user.getName();
            userPhone = user.getPhone();
        }
    }

    public void setLock(List<Lock> lock){
        if(lock !=null && lock.size()>0){
            lockNumber = lock.get(0).getSerialNumber();
        }
    }

    @Override
    public String getParklotTypeStr(){
        return Status.ParklotType.find(this.parklotType);
    }
}
