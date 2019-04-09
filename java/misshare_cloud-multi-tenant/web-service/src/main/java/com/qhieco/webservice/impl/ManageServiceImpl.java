package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Feedback;
import com.qhieco.commonrepo.FeedbackRepository;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.FeedbackRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.ManageService;
import com.qhieco.webservice.exception.ExcelException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 上午11:27
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class ManageServiceImpl implements ManageService{

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Override
    public Resp feedback(FeedbackRequest request){
        Page<Feedback> page = feedbackRepository.findAll(
                feedbackWhere(request),
                PageableUtil.pageable(request.getSEcho(), request.getStart(), request.getLength()));
        List<Feedback> dataList = page.getContent();
        for (Feedback adata:dataList
                ) {
            feedbackProperty(adata);
        }
        Integer count = (int) page.getTotalElements();
        AbstractPaged<Feedback> data = AbstractPaged.<Feedback>builder()
                .sEcho(request.getSEcho()+1)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .dataList(dataList).build();
        return RespUtil.successResp(data);
    }

    private Specification<Feedback> feedbackWhere(FeedbackRequest request){
        return (root, query, cb) ->{
            val page = new PageableUtil(root,query,cb);
            page.between("createTime",request.getStartCreateTime(),request.getEndCreateTime());
            if(request.getPhone()!=null && !"".equals(request.getPhone())){
                List<Integer> uid = feedbackRepository.findIdByPhone("%"+request.getPhone()+"%");
                page.in("mobileUserId",uid);
            }
            if(request.getProblems()!= null && request.getProblems().size()!=0) {
                for (Integer pId:request.getProblems()
                        ) {
                    List<Integer> feedbackIdList = feedbackRepository.findIdsByProblem(pId);
                    page.in("id",feedbackIdList);
                }
            }
            return page.pridect();
        };
    }

    private void feedbackProperty(Feedback data){
        val user = userMobileRepository.findOne(data.getMobileUserId());
        if (user!=null){
            data.setPhone(user.getPhone());
        }
        val parklot = parklotRepository.findOne(data.getParklotId());
        if (parklot!=null){
            data.setParklotName(parklot.getName());
        }
        val problems = feedbackRepository.findProblem(data.getId());
        data.setProblem(String.join(",", problems));
    }

    @Override
    public Resp feedbackExcel(FeedbackRequest feedbackRequest, OutputStream outputStream) throws IOException {
        List<Feedback> dataList = feedbackRepository.findAll(
                feedbackWhere(feedbackRequest));
        if (dataList.size() == 0){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        dataList.forEach(this::feedbackProperty);
        ExcelUtil<Feedback> excelUtil = new ExcelUtil<>(outputStream,Feedback.class);
        excelUtil.write(dataList);
        return  RespUtil.successResp();
    }
}
