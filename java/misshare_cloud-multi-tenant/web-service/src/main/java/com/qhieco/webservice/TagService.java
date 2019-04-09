package com.qhieco.webservice;

import com.qhieco.commonentity.File;
import com.qhieco.commonentity.Tag;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.TagManualData;
import com.qhieco.response.data.web.TagUserListData;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:39
 * <p>
 * 类说明：
 *     标签的Service
 */
public interface TagService {

    /**
     * 保存标签
     * @param tag
     * @return
     */
    Resp saveOrUpdate(Tag tag);

    /**
     * 分页查询标签列表
     * @param tagPageableRequest
     * @return
     */
    Resp pageable(TagPageableRequest tagPageableRequest);

    /**
     * 删除标签
     * @param tagId
     * @return
     */
    Resp delete(Integer tagId);

    /**
     * 查询所有有效的标签
     * @return
     */
    Resp all();

    /**
     * 导出标签
     * @param tagPageableRequest
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    Resp excel(TagPageableRequest tagPageableRequest, OutputStream outputStream, Class cl) throws IOException;


    /**
     * 查看标签用户数量
     * @param tagId
     * @return
     */
    Resp one(Integer tagId);

    /**
     * 分页查询消息列表
     * @param messagePageableRequest
     * @return
     */
    Resp messagePageable(MessagePageableRequest messagePageableRequest);


    /**
     * 导出消息
     * @param messagePageableRequest
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    Resp messageExcel(MessagePageableRequest messagePageableRequest, OutputStream outputStream, Class cl) throws IOException;


    /**
     * 保存文件数据
     * @param file
     * @return
     */
    Resp saveFile(File file);

    /**
     * 通过用户Id发送极光推送
     * @param sendJpushRequest
     * @return
     */
    Resp sendJpushByUserId(SendJpushRequest sendJpushRequest);

    /**
     * 通过标签Id发送极光推送
     * @param sendJpushRequest
     * @return
     */
    Resp sendJpushByTagId(SendJpushRequest sendJpushRequest);

    /**
     * 保存和更新手动标签
     *
     * @param request
     * @return
     */
    public Resp saveOrUpdateManualTag(ManualTagSaveRequest request);

    public AbstractPaged<TagUserListData> queryUserList(TagUserRequest request);

    public TagManualData queryManualTagInfoByTagId(Integer tagId);
}
