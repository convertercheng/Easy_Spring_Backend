package com.qhieco.webmapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 13:03
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface FileMapper {

    @Delete(value = "delete from t_file where id=#{id}")
    public void deleteById(Integer id);

    @Select(value = "select path from t_file where id=#{id}")
    public String queryPathById(Integer id);

    @Update(value = "update b_activity_file set file_id=#{newFileId} where file_id=#{oldFileId}")
    public void updateActivityFileB(@Param("oldFileId") Integer oldFileId, @Param("newFileId") Integer newFileId);

    @Update(value = "update b_activity_file set state=0 where file_id=#{oldFileId}")
    public void updateActivityFileBByFileId(@Param("oldFileId") Integer oldFileId);

    @Insert(value = "insert into b_activity_file(activity_id, file_id, state) values(#{activityId}, #{fileId}, #{state})")
    public void insertActivityFileB(@Param("activityId") Integer activityId, @Param("fileId") Integer fileId, @Param("state") Integer state);

    public List<String> queryFilepathsByIds(@Param("ids") String[] ids);
}
