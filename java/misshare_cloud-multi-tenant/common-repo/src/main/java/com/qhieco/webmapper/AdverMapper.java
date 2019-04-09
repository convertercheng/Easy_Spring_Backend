package com.qhieco.webmapper;

import com.qhieco.response.data.web.AdvertData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 11:57
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface AdverMapper {

    public AdvertData queryByPhoneType(@Param("phoneType") int phoneType);

    @Delete(value = "delete from t_advert where id=#{id}")
    public void deleteById(int id);

    @Select(value = "select id from t_advert where phone_type =#{phoneType}")
    public Integer queryIdByPhoneType(int phoneType);
}
