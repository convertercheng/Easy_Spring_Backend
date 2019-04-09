package com.qhieco.apiservice.impl;

import com.google.gson.Gson;
import com.qhieco.apiservice.BankCardService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.BankCard;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonrepo.BankCardRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.juhe.SingletonRestTemplate;
import com.qhieco.request.api.BankCardAddRequest;
import com.qhieco.request.api.BankCardUnbindRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.BankNameRepData;
import com.qhieco.response.data.api.VerifyBankCard4RepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:57
 * <p>
 * 类说明：
 * 银行卡service实现
 */
@Service
@Slf4j
public class BankCardServiceImpl implements BankCardService {

    @Autowired
    BankCardRepository bankCardRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Override
    public Resp addBankCard(BankCardAddRequest bankCardAddRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(bankCardAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        //验证手机用户是否存在
        Integer userId = bankCardAddRequest.getUser_id();
        UserMobile user = userMobileRepository.findOne(userId);
        if(null == user){
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        //验证用户是否已经绑定过银行卡
        List<BankCard> bankCards = bankCardRepository.findByMobileUserIdAndState(userId,Status.Common.VALID.getInt());
        if(bankCards.size() != 0){
            throw new QhieException(Status.ApiErr.REPEAT_BANKCARD_ADD);
        }
        String reservedPhone = bankCardAddRequest.getReserved_phone();
        String bankNumber = bankCardAddRequest.getBank_number();
        String bank = bankCardAddRequest.getBank();
        String type = bankCardAddRequest.getType();
        //聚合四元素验证银行卡
        String returnJson = this.verifyBankCard4(user,bankNumber,reservedPhone);
        log.info(returnJson);
        Gson gson = new Gson();
        VerifyBankCard4RepData resDate = gson.fromJson(returnJson, VerifyBankCard4RepData.class);
        Integer errorCode = resDate.getError_code();
        String res = resDate.getResult().getRes();
        //验证匹配失败
        if(!Constants.JUHE_ERROR_CODE_SUCCESS.equals(errorCode)){
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
        //验证结果不匹配
        if(Constants.VERIFYBANKCARD4_RESULT_MISMATCH.equals(res)){
            throw new QhieException(Status.ApiErr.MISMATCH_VERIFYBANKCARD4);
        }
        //验证通过，保存银行卡
        BankCard bankCard = new BankCard(userId,user.getName(), reservedPhone,bankNumber,bank,type,Status.Common.VALID.getInt(),System.currentTimeMillis());
        BankCard resBankCard = bankCardRepository.save(bankCard);
        if(null == resBankCard){
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return RespUtil.successResp();
    }


    @Override
    public Resp getBankName(BankCardAddRequest bankCardAddRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(bankCardAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String returnJson = this.silkBankCard(bankCardAddRequest.getBank_number());
        log.info(returnJson);
        Gson gson = new Gson();
        BankNameRepData resDate = gson.fromJson(returnJson, BankNameRepData.class);
        return RespUtil.successResp(resDate);
    }

    /**
     * 从聚合获取银行信息
     * @param bankNumber 银行卡号
     * @return
     */
    private String silkBankCard(String bankNumber) {
        RestTemplate restTemplate = SingletonRestTemplate.getInstance().getRestTemplate();
        try {
            return restTemplate.getForObject(Constants.URL_JUHE_BANKCARDSILK, String.class,
                            bankNumber,
                            Constants.VALUE_BANKCARDSILK_KEY

                    );
        } catch (Exception e){
            e.printStackTrace();
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
    }

    /**
     * 聚合四元素验证银行卡
     * @param user 手机用户
     * @param bankNumber  银行卡号
     * @param reservedPhone 银行预留电话号码
     * @return
     */
    private String verifyBankCard4(UserMobile user,String bankNumber,String reservedPhone){
        RestTemplate restTemplate = SingletonRestTemplate.getInstance().getRestTemplate();
        try {
            return  restTemplate.getForObject(Constants.URL_JUHE_VERIFYBANKCARD4, String.class,
                            Constants.VALUE_VERIFYBANKCARD4_KEY,
                            bankNumber,
                            user.getName(),
                            user.getIdentityNumber(),
                            reservedPhone
                    );
        } catch (Exception e) {
            e.printStackTrace();
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
    }

    @Override
    public Resp unbind(BankCardUnbindRequest bankCardUnbindRequest) {
        String timestamp = bankCardUnbindRequest.getTimestamp();
        if (StringUtils.isEmpty(timestamp) || CommonUtil.isTimeStampInValid(timestamp)) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer bankCardId = bankCardUnbindRequest.getBankcard_id();
        Integer userId = bankCardUnbindRequest.getUser_id();
        BankCard bankCard = bankCardRepository.findOne(bankCardId);
        if (bankCard == null) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_BANKCARD);
        }
        if (!bankCard.getMobileUserId().equals(userId)) {
            throw new QhieException(Status.ApiErr.MISMATCH_UNBIND_BANKCARD);
        }
        bankCard.setState(Status.Common.INVALID.getInt());
        if (null == bankCardRepository.save(bankCard)) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return RespUtil.successResp();
    }

}
