package com.qhieco.webmapper;

import com.qhieco.commonentity.relational.UserTagB;
import com.qhieco.response.data.web.TagManualData;
import com.qhieco.response.data.web.TagUserListData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/15 15:47
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface TagMapper {

    public int countRepeatTagName(@Param("tagId") Integer tagId, @Param("tagName") String tagName);

    @Update(value = "UPDATE b_user_tag SET state=0 WHERE tag_id=#{tagId} AND state=#{state}")
    public void updateStateByCondition(@Param("tagId") Integer tagId, @Param("state") Integer state);

    public void insertBatchUserTag(@Param("userTagList") List<UserTagB> userTagList);

    public List<TagUserListData> queryTagUserList(HashMap<String, Object> params);

    public int queryCountTagUser(HashMap<String, Object> params);

    public TagManualData queryTagManualInfoByTagId(@Param("tagId") Integer tagId);

    @Select(value = "SELECT but.`mobile_user_id` userId FROM b_user_tag but WHERE but.`tag_id`= #{tagId} AND but.`state`=1")
    public List<Integer> queryBindUsersByTagId(@Param("tagId") Integer tagId);

    public List<TagUserListData> queryBindUserInfoByTagId(@Param("tagId") Integer tagId);
}
