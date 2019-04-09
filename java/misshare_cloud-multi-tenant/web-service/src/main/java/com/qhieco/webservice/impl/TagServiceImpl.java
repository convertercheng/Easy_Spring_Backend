package com.qhieco.webservice.impl;


import com.qhieco.commonentity.File;
import com.qhieco.commonentity.Message;
import com.qhieco.commonentity.Tag;
import com.qhieco.commonentity.relational.MessageFileB;
import com.qhieco.commonentity.relational.UserTagB;
import com.qhieco.commonrepo.*;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.QhPushUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.TagMapper;
import com.qhieco.webmapper.UserMapper;
import com.qhieco.webservice.TagService;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageFileBRepository messageFileBRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserTagBRepository userTagBRepository;

    @Autowired
    private TagMapper tagMapper;

    @PersistenceContext
    EntityManager em;


    private QueryFunction<Tag, TagPageableRequest> queryFunctionTag;

    private QueryFunction<Message, MessagePageableRequest> queryFunctionMessage;

    @PostConstruct
    public void init() {
        queryFunctionTag = new QueryFunction<>(tagRepository);
        queryFunctionTag.setTransientProperty(this::transientPropertyTag);

        queryFunctionMessage = new QueryFunction<>(messageRepository);
        queryFunctionMessage.setTransientProperty(this::transientPropertyMessage);
    }

    private Tag transientPropertyTag(Tag data) {
        val newData = new Tag();
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }

    private Message transientPropertyMessage(Message data) {
        val newData = new Message();
        if (data.getMobileUserId() != null) {
            data.setPhone(userMobileRepository.findOne(data.getMobileUserId()).getPhone());
        }
        String type = "";
        if (data.getType().equals(Constants.MESSAGE_TYPE_ACTIVITY)) {
            type = "活动消息";
        } else if (data.getType().equals(Constants.MESSAGE_TYPE_PERSONAL)) {
            type = "个人消息";
        }
        String kind = "";
        if (data.getKind().equals(Constants.MESSAGE_KIND_JPUSH)) {
            kind = "APP推送";
        } else if (data.getKind().equals(Constants.MESSAGE_KIND_SMS)) {
            kind = "短信推送";
        }
        data.setTypeAndKind(kind + "/" + type);
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp saveOrUpdate(Tag tag) {
        tag.setType(Status.TagType.AUTOMATIC.getInt());
        Long now = System.currentTimeMillis();
        List<Tag> tagList = tagRepository.findByNameAndState(tag.getName(), Status.Common.VALID.getInt());
        Integer id = tag.getId();
        //验证标签名称是否存在
        if (null != tagList && tagList.size() != Constants.EMPTY_CAPACITY) {
            if (null == id) {
                return RespUtil.errorResp(Status.WebErr.TAG_NAME_EXISTS.getCode(), Status.WebErr.TAG_NAME_EXISTS.getMsg());
            } else {
                for (Tag tagDB : tagList) {
                    if (!id.equals(tagDB.getId())) {
                        return RespUtil.errorResp(Status.WebErr.TAG_NAME_EXISTS.getCode(), Status.WebErr.TAG_NAME_EXISTS.getMsg());
                    }
                }
            }
        }
        Tag dest = tag;
        if (id != null) {
            dest = tagRepository.findOne(id);
            dest.setName(tag.getName());
            dest.setComment(tag.getComment());
            dest.setEndSignupTime(tag.getEndSignupTime());
            dest.setOrderAmount(tag.getOrderAmount());
            dest.setOrderNumber(tag.getOrderNumber());
            dest.setStartSignupTime(tag.getStartSignupTime());
            dest.setUnsigninDays(tag.getUnsigninDays());
            dest.setUserType(tag.getUserType());
            if (dest == null) {
                return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
            }
        }
        if (dest.getState() == null) {
            dest.setState(Status.Common.VALID.getInt());
        }
        if (id == null) {
            dest.setCreateTime(now);
        }
        dest.setModifyTime(now);
        return RespUtil.successResp(tagRepository.save(dest));
    }

    @Override
    public Resp pageable(TagPageableRequest tagPageableRequest) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return queryFunctionTag.queryOrder(tagPageableRequest, tagWhere(tagPageableRequest), sort);
    }

    private Specification<Tag> tagWhere(TagPageableRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            page.equal("state", Status.Common.VALID.getInt());
            if (StringUtils.isNotEmpty(request.getName())) {
                page.like("name", request.getName());
            }
            return page.pridect();
        };
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp delete(Integer tagId) {
        tagRepository.deleteTag(tagId, Status.Common.DELETED.getInt());
        return RespUtil.successResp();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp all() {
        return RespUtil.successResp(tagRepository.all());
    }


    @Override
    public Resp excel(TagPageableRequest tagPageableRequest, OutputStream outputStream, Class cl) throws IOException {
        return queryFunctionTag.excel(tagWhere(tagPageableRequest), cl, outputStream);
    }


    @Override
    public Resp one(Integer tagId) {
        long time = System.currentTimeMillis();
        TagUserNumberData data = new TagUserNumberData();
        Tag tag = tagRepository.findOne(tagId);
        if (null == tag) {
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        Integer number = userMapper.findTagUserNumber(tag);
        data.setNumber(number);
        return RespUtil.successResp(data);
    }


    @Override
    public Resp messagePageable(MessagePageableRequest messagePageableRequest) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        return queryFunctionMessage.queryOrder(messagePageableRequest, messageWhere(messagePageableRequest), sort);
    }

    private Specification<Message> messageWhere(MessagePageableRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            page.equal("state", Status.Common.VALID.getInt());
            if (StringUtils.isNotEmpty(request.getPhone())) {
                val ids = userMobileRepository.findByPhoneIds("%" + request.getPhone() + "%");
                page.in("mobileUserId", ids);
            }
            if (StringUtils.isNotEmpty(request.getContent())) {
                page.like("content", request.getContent());
            }
            if (request.getStartTime() != null && request.getEndTime() != null) {
                page.between("createTime", request.getStartTime(), request.getEndTime());
            }
            return page.pridect();
        };
    }

    @Override
    public Resp messageExcel(MessagePageableRequest messagePageableRequest, OutputStream outputStream, Class cl) throws IOException {
        return queryFunctionMessage.excel(messageWhere(messagePageableRequest), cl, outputStream);
    }

    @Override
    public Resp saveFile(File file) {
        File resFile = fileRepository.save(file);
        if (null == resFile) {
            RespUtil.errorResp(Status.WebErr.INSERT_ERROR.getCode(), Status.WebErr.INSERT_ERROR.getMsg());
        }
        return RespUtil.successResp(resFile.getId());
    }

    @Override
    public Resp sendJpushByUserId(SendJpushRequest sendJpushRequest) {
        Long now = System.currentTimeMillis();
        Integer webUserId = sendJpushRequest.getWebUserId();
        String userIds = sendJpushRequest.getUserIds();
        String content = sendJpushRequest.getContent();
        String title = sendJpushRequest.getTitle();
        Integer type = sendJpushRequest.getType();
        String href = sendJpushRequest.getHref();
        Integer fileId = sendJpushRequest.getFileId();
        List<UserMessageData> userMessageDataList = userMapper.findMessageData(userIds);
        List<String> jpushIdList = new ArrayList<>();
        for (UserMessageData userMessageData : userMessageDataList) {
            String jpushId = userMessageData.getJpushId();
            if (StringUtils.isNotEmpty(jpushId)) {
                jpushIdList.add(jpushId);
            }
            Message message = new Message(userMessageData.getUserId(), webUserId, title, content, href, type, Constants.MESSAGE_KIND_JPUSH, Status.Common.VALID.getInt(), now);
            Message resMessage = messageRepository.save(message);
            if (null != resMessage && null != fileId && 0 != fileId) {
                MessageFileB messageFileB = new MessageFileB(resMessage.getId(), fileId, Status.Common.VALID.getInt(), now);
                messageFileBRepository.save(messageFileB);
            }
        }
        QhPushUtil.getInstance().sendQhPushBatch(jpushIdList, QhMessageType.ANNOUNCEMENT, title + " " + content, "");

        return RespUtil.successResp();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp sendJpushByTagId(SendJpushRequest sendJpushRequest) {
        Long now = System.currentTimeMillis();
        Integer webUserId = sendJpushRequest.getWebUserId();
        String tagIds = sendJpushRequest.getTagIds();
        String[] tagIdStringChar = tagIds.split(Constants.DELIMITER_COMMA);
        List<Integer> tagIdList = new ArrayList<>();
        for (String tagId : tagIdStringChar) {
            tagIdList.add(Integer.valueOf(tagId));
        }
        String content = sendJpushRequest.getContent();
        String title = sendJpushRequest.getTitle();
        Integer type = sendJpushRequest.getType();
        String href = sendJpushRequest.getHref();
        Integer fileId = sendJpushRequest.getFileId();
        List<Tag> tagList = tagRepository.findByIds(tagIdList);
        Set<Integer> userIdSet = new HashSet<>();
        for (Tag tag : tagList) {
            List<Integer> userIdList;
            if (Status.TagType.AUTOMATIC.getInt().toString().equals(tag.getType())) {
                userIdList = userMapper.findTagUserId(tag);
            } else {
                userIdList = userTagBRepository.findByTagId(tag.getId());
            }
            userIdSet.addAll(userIdList);
        }
        String ids = StringUtils.join(userIdSet.toArray(), ",");
        if (!StringUtils.isNotEmpty(ids)) {
            return RespUtil.successResp();
        }
        List<UserMessageData> userMessageDataList = userMapper.findMessageData(ids);
        List<Message> messageList = new ArrayList<>();
        List<String> jpushIdList = new ArrayList<>();
        for (UserMessageData data : userMessageDataList) {
            String jpushId = data.getJpushId();
            if (StringUtils.isNotEmpty(jpushId)) {
                jpushIdList.add(jpushId);
            }
            Message message = new Message(data.getUserId(), webUserId, title, content, href, type, Constants.MESSAGE_KIND_JPUSH, Status.Common.VALID.getInt(), now);
            messageList.add(message);
        }
        QhPushUtil.getInstance().sendQhPushBatch(jpushIdList, QhMessageType.ANNOUNCEMENT, title + " " + content, "");
        List<Integer> messageIdList = this.batchInsertMessage(messageList);
        if (messageIdList.size() > Constants.EMPTY_CAPACITY && null != fileId && 0 != fileId) {
            List<MessageFileB> messageFileBList = new ArrayList<>();
            for (Integer messageId : messageIdList) {
                MessageFileB messageFileB = new MessageFileB(messageId, fileId, Status.Common.VALID.getInt(), now);
                messageFileBList.add(messageFileB);
            }
            this.batchInsertMessageFileB(messageFileBList);
        }
        return RespUtil.successResp();
    }

    /**
     * MessageFileB
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertMessageFileB(List<MessageFileB> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            MessageFileB dd = list.get(i);
            em.persist(dd);
            // 每1000条数据执行一次，或者最后不足1000条时执行
            if (i % 1000 == 0 || i == (size - 1)) {
                em.flush();
                em.clear();
            }
        }
    }

    /**
     * Message
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> batchInsertMessage(List<Message> list) {
        int size = list.size();
        List<Integer> messageIdList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Message dd = list.get(i);
            em.persist(dd);
            messageIdList.add(dd.getId());
            // 每1000条数据执行一次，或者最后不足1000条时执行
            if (i % 1000 == 0 || i == (size - 1)) {
                em.flush();
                em.clear();
            }
        }
        return messageIdList;
    }

    /**
     * 保存和更新手动标签
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp saveOrUpdateManualTag(ManualTagSaveRequest request) {
        // 1 判断标签名称是否重复
        int countRepeat = tagMapper.countRepeatTagName(request.getTagId(), request.getTagName());
        if (countRepeat > 0) {
            return RespUtil.errorResp(Status.WebErr.TAG_NAME_EXISTS.getCode(), Status.WebErr.TAG_NAME_EXISTS.getMsg());
        }

        // 2 保存标签信息
        Tag tag = saveTag(request);

        // 3 更新全部的绑定用户 - 先解除原有的所有关系 -再关联新的关系
        if (request.getTagId() != null) {
            tagMapper.updateStateByCondition(request.getTagId(), Status.Common.VALID.getInt());
        }

        // 去掉重复的userId
        List<Integer> oldUserIds = request.getUserIds();
        Set<Integer> userids = new HashSet<>();
        for (Integer userId : oldUserIds) {
            userids.add(userId);
        }

        List<UserTagB> userTagBList = new ArrayList<>();
        UserTagB userTagB = null;
        Long now = System.currentTimeMillis();
        for (Integer userId : userids) {
            if (userId != null) {
                userTagB = new UserTagB();
                userTagB.setTagId(tag.getId());
                userTagB.setMobileUserId(userId);
                userTagB.setState(Status.Common.VALID.getInt());
                userTagB.setCreateTime(now);
                userTagB.setModifyTime(now);
                userTagBList.add(userTagB);
            }
        }
        tagMapper.insertBatchUserTag(userTagBList);
        return RespUtil.successResp();
    }

    public Tag saveTag(ManualTagSaveRequest request) {
        Tag tag = new Tag();
        if (request.getTagId() == null) {
            tag.setCreateTime(System.currentTimeMillis());
            tag.setState(Status.Common.VALID.getInt());
        } else {
            tag = tagRepository.findOne(request.getTagId());
        }
        tag.setName(request.getTagName());
        tag.setComment(request.getComment());
        tag.setModifyTime(System.currentTimeMillis());
        tag.setType(Status.TagType.MANUAL.getInt());
        tag.setOrderAmount(BigDecimal.ZERO);
        tag = tagRepository.save(tag);
        return tag;
    }

    @Override
    public AbstractPaged<TagUserListData> queryUserList(TagUserRequest request) {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("tagId", request.getTagId());
        params.put("phone", request.getPhone());
        params.put("userType", request.getUserType());
        params.put("startPage", request.getStart());
        params.put("pageSize", request.getLength());

        int count = tagMapper.queryCountTagUser(params);
        List<TagUserListData> tagUserListDataList = null;
        if (count > 0) {
            tagUserListDataList = tagMapper.queryTagUserList(params);
        }

        AbstractPaged<TagUserListData> data = AbstractPaged.<TagUserListData>builder()
                .sEcho(request.getSEcho() + 1)
                .dataList(tagUserListDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public TagManualData queryManualTagInfoByTagId(Integer tagId) {
        TagManualData tagManualData = tagMapper.queryTagManualInfoByTagId(tagId);
        List<Integer> bindUsers = tagMapper.queryBindUsersByTagId(tagId);
        List<TagUserListData> tagUserListDataList = tagMapper.queryBindUserInfoByTagId(tagId);
        if (tagManualData != null) {
            tagManualData.setBindUsers(bindUsers);
            tagManualData.setTagUserListDataList(tagUserListDataList);
        }
        return tagManualData;
    }
}
