package com.qhieco.push;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 上午11:00
 * <p>
 * 类说明：
 *     激光推送模板
 */
public class QhMessageTemplate {
    /**
     *  reserve
      */
    public static final String ENTER_PROMPT = "尊敬的用户，感谢您预定%s车位，现距最晚时间还有15分钟，请您准时入场，因车位紧张超时将不可入场，感谢您的支持。";
    public static final String LEAVE_PROMPT = "尊敬的用户，您预约的%s共享车位还有15分钟到时，因车位预约紧张，请您按时驶离车位，请勿超时占用车位，感谢您的支持。";
    public static final String TIMEOUT_PROMPT = "尊敬的用户，您预约的%s共享车位已经超时5分钟，为了方便其他用户的使用，请您尽快驶离车位，超时占用车位将加倍外收取停车费用，感谢您的支持。";
    public static final String CANCEL_PROMPT = "尊敬的用户，您预约的%s共享车位已过最晚入场时间，系统自动为您取消该预约信息，如您还需车位请重新预约，感谢您的支持。";

    /**
     *  wallet
     */
    public static final String LEAVE_PAY_PROMPT = "尊敬的用户，您已驶离%s车位，预约订单结束，请在享你了APP支付该订单的停车费用，感谢您的支持。";
    public static final String APPLY_PARKLOC_PROCESSING = "您添加的车位信息已经提交审核，工作人员会及时和您联系，请保持电话畅通。";
    public static final String APPLY_PARKLOC_SUCCESS = "尊敬的用户，您的%s车位已经通过审核，感谢您的分享。";
    /**
     * message
     */
    public static final String ENTER = "enter";
    public static final String LEAVE = "leave";
    public static final String SIGIN_OUT = "sigin_out";

}
