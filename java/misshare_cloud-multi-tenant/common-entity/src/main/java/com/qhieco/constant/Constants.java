package com.qhieco.constant;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 上午11:00
 * <p>
 * 类说明：
 *     常量类
 */

public class Constants {

    /**
     * 通用
     */
    public static final int EMPTY_CAPACITY = 0;
    public static final String EMPTY_STRING = "";
    public static final int FIRST_INDEX = 0;
    public static final Integer ONE_CAPACITY = 1;
    public static final int MIN_NON_NEGATIVE_INTEGER = 0;
    public static final int DOUBLE_ONE = 1;
    public static final BigDecimal BIGDECIMAL_ONE = new BigDecimal("1");
    public static final BigDecimal BIGDECIMAL_ONE_HUNDRED = new BigDecimal("100");
    public static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal("0.00");
    public static final double MIN_POSITIVE_DOUBLE = 0.00;
    public static final String FORMAT_JSON = "json";
    public static final String ENCODING_FORMAT_UTF_8 = "UTF-8";
    public static final String SUCCESS = "success";
    public static final int DECIMAL_PLACE_DEFAULT = 2;
    public static final int DELETE_MARK = -1;
    public static final String TIME_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ORDER_PARKING_ID = "orderParkingId";

    /**
     * 广告页
      */
    public static final String ADVERT_FILE_PATH = "/img/ad/";
    public static final String FILE_PREFIX = "file:";

    /**
     * 时间差
     */
    public static final Long TIMESTAMP_MAX_DIFF_C_AND_S = 5 * 60 * 1000L;
    public static final Long TIMESTAMP_MAX_DIFF_VERIFICATION = 3 * 60 * 1000L;
    public static final Long TIMESTAMP_ONE_DAY = 24*60*60*1000L;

    /**
     * 验证码相关
     */
    public static final int VERIFICATION_CODE_NUM_LIMIT = 4;
    public static final String VERIFICATION_CODE_NUM_RANGE = "0123456789";
    public static final int MAX_VERIFICATION_SEND_NUM = 2;
    public static final int ANTI_GET_VERIFICATION = 1;

    /**
     * 聚合SMS API的相关常量
     */
    public static final String URL_JUHE_SMS = "http://v.juhe.cn/sms/send?mobile={phone}&tpl_id={tpl_id}&tpl_value={tpl_value}&key={key}";
    public static final String URL_JUHE_BANKCARDSILK = "http://bankcardsilk.api.juhe.cn/bankcardsilk/query.php?num={num}&key={key}";
    public static final String URL_JUHE_VERIFYBANKCARD4 = "http://v.juhe.cn/verifybankcard4/query.php?key={key}&bankcard={bankcard}&realname={realname}&idcard={idcard}&mobile={mobile}";
    public static final Integer VALUE_TPL_ID = 41356;
    public static final String VALUE_TPL_VALUE_TEMPLATE = "#code#=%s";
    public static final String VALUE_SMS_KEY = "c718da9eb368b06d145a81f5661e093d";
    public static final String VALUE_BANKCARDSILK_KEY = "1958b98f34bfaca4f73a3aea2efa6d74";
    public static final String VALUE_VERIFYBANKCARD4_KEY = "d4dcafede1e7fd14e3cf8d7269c1e794";
    public static final String VERIFYBANKCARD4_RESULT_MATCH = "1";
    public static final String VERIFYBANKCARD4_RESULT_MISMATCH = "2";
    public static final Integer JUHE_ERROR_CODE_SUCCESS = 0;

    /**
     * 聚合数据上的短信模版
     */
    public static final String TEMPLATE_JUHE_SMS = "【享你了】验证码%s。如非本人操作，请忽略本短信[10分钟内有效]";

    /**
     * 阿里云通信的短信模板
     */
    public static final String TEMPLATE_ALI_SMS = "【享你了APP]验证码%s,您正在登录，若非本人操作，请勿泄露。";

    /**
     * 短信类型
     */
    public static final int SMS_TYPE_VERIFICATION = 0;

    /**
     * token
     */
    public static final String TOKEN = "akjsdfjaksdjf1232h42323klj2kjkfjdaklLOJIDFJDJHD)HJKJHJDKLjkdajfkljas)nkafa0";

    /**
     * 用户类型 0：车主 1：业主 2：管理员
     */
    public static final byte PARKING_RENTEE = 0;
    public static final byte PARKING_RENTER = 1;
    public static final byte PARKING_ADMIN = 2;
    public static final Integer USER_LEVEL=60;

    /**
     * 常用停车场等级
     */
    public static final Integer PARKING_USUAL_FIRST = 1;
    public static final Integer PARKING_USUAL_SECOND = 2;
    public static final Integer PARKING_USUAL_THIRD = 3;
    public static final Integer PARKING_USUAL_FOURTH = 4;
    //默认
    public static final Integer PARKING_USUAL_FIFTH = 5;

    /**
     * 经纬度相关
     */
    public static final Double ERATH_RADIUS = 6371d;
    public static final int ANGLE_HALF_CIRCLE = 180;
    public static final int ONE_KILOMETER = 1000;

    /**
     * 停车场类型，0是静态车场车位查询，1是约车场，2是约车位
     */
    public static final byte PARKING_STATIC = 0;
    public static final byte PARKING_APPOINTMENT = 1;
    public static final byte PARKING_SHARE = 2;
    public static final Integer PARKING__APPOINTMENT_INNER = 3;
    public static final Integer PARKING_SHARE_INNER = 4;

    /**
     * 系统参数key
     */
    public static final String TAX_RATE = "tax_rate";
    public static final String ALIPAY_FEE_RATE = "alipay_fee_rate";
    public static final String WXPAY_FEE_RATE = "wxpay_fee_rate";

    /**
     * 自动取消未支付的预约订单时长：五分钟
     */
    public static final long AUTO_CANCEL_RESERVED_TIME = 300000;

    /**
     * 小区参数key
     */
    /**
     * 时间组
     */
    public static final String TIME_CODE="time_code";
    /***    发布时段最小时间间隔        **/
    public static final String MIN_PUBLISH_INTERVAL = "min_publish_interval";
    /***    提前一个时间更换用户预约的停车位    */
    public static final String ADVANCE_CHANGE_TIME = "advance_change_time";
    /**     业主发布的车位共享时间的最短时长，缺省值30分钟    */
    public static final String MIN_SHARING_PERIOD = "min_sharing_period";
    /**     共享车位被使用，最短计费时间，缺省值15分钟     */
    public static final String MIN_CHARGING_PERIOD = "min_charging_period";
    /**     免费取消预约的提前时间，缺省值为0分钟    */
    public static final String FREE_CANCELLATION_TIME = "free_cancellation_time";
    /**     已到入场时间但车主未入场，为车主保留车位的时长。    */
    public static final String MAX_DELAY_TIME = "max_delay_time";
    /**     车位可以提前多长时间预约。缺省值120分钟   */
    public static final String ADVANCE_RESERVATION_TIME = "advance_reservation_time";

    /**
     * 平台组
     */
    public static final String ALLOCATION_CODE ="allocation_code";
    /**     业主分成    */
    public static final String OWNER_PERCENTAGE = "owner_percentage";
    /**     物业分成    */
    public static final String PROPCOMP_PERCENTAGE = "propcomp_percentage";
    /**     平台分成    */
    public static final String PLATFORM_PERCENTAGE = "platform_percentage";
    /**     运营商分成  */
    public static final String OPERATOR_PERCENTAGE = "operator_percentage";
    /**     业主为管理员时物业分成    */
    public static final String PROPCOMP_APPOINTMENT_PERCENTAGE = "propcomp_appointment_percentage";
    /**     业主为管理员时平台分成    */
    public static final String PLATFORM_APPOINTMENT_PERCENTAGE = "platform_appointment_percentage";

    /**
     * 系统参数缺省值
     */
    public static final String MIN_PUBLISH_INTERVAL_DEFAULT = "30";
    public static final String MIN_SHARING_PERIOD_DEFAULT = "30";
    public static final String MIN_CHARGING_PERIOD_DEFAULT = "15";
    public static final String ADVANCE_CHANGE_TIME_DEFAULT = "0";
    public static final String FREE_CANCELLATION_TIME_DEFAULT = "0";
    public static final String MAX_DELAY_TIME_DEFAULT = "5";
    public static final String ADVANCE_RESERVATION_TIME_DEFAULT = "0";
    public static final String OWNER_PERCENTAGE_DEFAULT = "60";
    public static final String PROPCOMP_PERCENTAGE_DEFAULT = "15";
    public static final String PLATFORM_PERCENTAGE_DEFAULT = "15";
    public static final String OPERATOR_PERCENTAGE_DEFAULT = "10";
    public static final String PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT = "0";
    public static final String PLATFORM_APPOINTMENT_PERCENTAGE_DEFAULT = "0";
    /**
     * 航天发票接口相关
     */
    public static final String SUCCESS_CODE = "0000";
    public static final String SUCCESS_MESSAGE= "开票成功";
    public static final String INVOICE_FILE_INTRO = "电子发票PDF文件";
    public static final String INVOICE_MAIL_TITLE = "您收到一张【前海爱翼科技（深圳）有限公司】开具的发票";

    /**
     * 优惠券状态
     */
    public static final  Integer MAKE_CODE=1900;
    public static final  Integer NOT_MAKE_CODE=1901;

    /**
     * 收费标准
     */
    public enum FeeRuleType {
        GUARANTEE,
        RESERVE,
        PARKING_PER_HOUR
    }

    /**
     * 分页数据大小
     */
    public static final int PAGE_SIZE = 20;
    public static final int PAGE_SIZE_50 = 50;
    public static final int EXCEL_SIZE = 5000;

    /**
     * 地域的级别
     */
    public enum AreaLevel {
        /**
         * 更上层
         */
        NATION,
        /**
         * 省
         */
        PROVINCE,
        /**
         * 市
         */
        CITY,
        /**
         * 区
         */
        REGION
    }

    /**
     * 发布规则（mode）
     */
    public static final Integer SINGLE_MODE = 0;
    public static final Integer LOOP_MODE = 1;

    /**
     * 车牌号
     */
    public static final String DELIMITER_COMMA = ",";
    public static final String DELIMITER_POUND = "#";
    public static final String DELIMITER_DOT = ".";

    /**
     * 各种文件的目录路径
     */
    public static final String DIRECTORY_AVATAR = "avatar/";
    public static final String DIRECTORY_PARKLOT = "parklot/";
    public static final String DIRECTORY_ADVERT = "advert/";
    public static final String DIRECTORY_ACTIVITY = "activity/";
    public static final String DIRECTORY_FEEDBACK = "feedback/";

    /**
     * 订单类型  0：预约订单 1：停车订单 2：退款订单 3：充值订单 4：提现订单
     */
    public static final Integer RESERVATION_ORDER = 0;
    public static final Integer PARKING_ORDER = 1;
    public static final Integer REFUND_ORDER = 2;
    public static final Integer TOPUP_ORDER = 3;
    public static final Integer WITHDRAW_ORDER = 4;

    /**
     * 支付类型 5：html5端支付
     */
    public static final Integer PAY_TYPE_HTML5 = 5;

    /**
     * 支付相关
     */
    public static final Integer PAY_CHANNEL_NONE = 0;
    public static final Integer PAY_CHANNEL_ALIPAY = 1;
    public static final Integer PAY_CHANNEL_WXPAY = 2;
    public static final Integer PAY_CHANNEL_CASH = 3;
    public static final Integer PAY_CHANNEL_COUPON = 4;
    public static final Integer PAY_CHANNEL_WXPAY_PUBLIC  = 5;
    public static final Integer PAY_CHANNEL_WXPAY_XCX  = 6;
    public static final String BODY = "享你了";
    public static final String SUBJECT = "享你了";
    public static final String CHARGE_BODY = "享你了-账户充值";
    public static final String RESERVE_BODY = "享你了-支付预约费";
    public static final String PARKING_BODY = "享你了-支付停车费";

    /**
     * 支付宝相关参数
     */
    public static final String ALIPAY_SERVER_URL = "https://openapi.alipay.com/gateway.do";
    public static final String ALIPAY_SIGN_TYPE = "RSA2";
    public static final String ALIPAY_NOTIFY_URL = "/apiwrite/alipay/callback/";
    public static final String ALIPAY_TIMEOUT_EXPRESS = "30m";
    public static final String ALIPAY_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    /**
     * 微信相关参数
     */
    public static final String WXPAY_KEY = "KK0ohDRaXFnbUlSU3ah9BIclPN2W6ei9";
    public static final String WXPAY_REFUND_ACCOUNT = "REFUND_SOURCE_RECHARGE_FUNDS";
    public static final String WXPAY_REFUND_ACCOUNTS = "REFUND_SOURCE_UNSETTLED_FUNDS";
    public static final String WXPAY_NOTIFY_URL = "/apiwrite/wxpay/callback";
    public static final String WXPAY_TRADE_TYPE = "APP";
    public static final String WXPAY_TRADE_TYPE_H5 = "MWEB";
    public static final String WXPAY_PACKAGE_VALUE = "Sign=WXPay";
    public static final String WXPAY_SERVER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WXPAY_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String WXPAY_REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    public static final String WXPAY_AUTHOR_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static  final String WX_ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static  final String WX_AUTHOR_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    public static final String WXPAY_XCX_AUTHOR_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    public static final String WXPAY_RETURN_CODE_SUCCESS = "SUCCESS";


    /**
     * 车锁相关
     */
    public static final String LOCK_COMMAND_UP = "[01:01]";
    public static final String LOCK_COMMAND_DOWN = "[01:02]";
    public static final String LOCK_COMMAND_BEE = "[0A:05]";
    public static final String LOCK_COMMAND_DOWN_DISABLE = "[05:22]";
    public static final String LOCK_COMMAND_ENABLE = "[05:01]";
    public static final String LOCK_UID = "c55e365d3f164df1";
    public static final String NB_LOT_LOCK_UID = "00e02f46ae2249a0";
    public static final String LOCK_SUBSCRIBE_PREFIX = "/status/lock/" + LOCK_UID + "/#";
    public static final String LOCK_PUBLISH_PREFIX = "/set/lock/" + LOCK_UID + "/";
    public static final String NB_IOT_LOCK_SUBSCRIBE_PREFIX = "/status/lock/" + NB_LOT_LOCK_UID;
    public static final String NB_IOT_LOCK_PUBLISH_PREFIX = "/set/lock/" + NB_LOT_LOCK_UID;
    public static final String LOCK_DATA = "DATA";
    public static final Integer LOCK_ERROR_CODE_CHAR_INDEX = 2;
    public static final Integer LOCK_ERROR_CODE_OK = 0;
    public static final Integer LOCK_BATTERYHEX_INDEX_START = 7;
    public static final Integer LOCK_BATTERYHEX_INDEX_END = 11;
    public static final Integer LOCK_POSITION_INDEX = 6;
    public static final Integer LOCK_BUZZERSTATE_INDEX = 5;
    public static final Integer LOCK_WORKSTATE_INDEX = 11;

    /**
     * 注册标记来源
     */
    public static final String USER_REGISTERONE="1";
    public static final String USER_REGISTERTWO="2";

    /**
     * 是否是工作日
     */
    public enum DayType {
        /**
         * 周末
         */
        WEEKEND,
        /**
         * 工作日
         */
        WEEKDAY
    }

    /**
     * 安装继电器为1，否则为0
     */
    public enum HasRelayMeter {
        /**
         * 没有安装继电器
         */
        NO,
        /**
         * 安装继电器
         */
        YES
    }

    /**
     * 入场和出场的tag
     */
    public static final String TAG_LEAVE = "leave";
    public static final String TAG_ENTER = "enter";
    /**
     * 车锁入场和出场的tag
     */
    public static final Integer TAG_LOCK_LEAVE = 1;
    public static final Integer TAG_LOCK_ENTER = 2;

    /**
     * 消息类型
     */
    public static final Integer MESSAGE_TYPE_PERSONAL = 0;
    public static final Integer MESSAGE_TYPE_ACTIVITY = 1;

    /**
     * 消息推送途径
     */
    public static final Integer MESSAGE_KIND_SMS = 0;
    public static final Integer MESSAGE_KIND_JPUSH = 1;

    /**
     * 报表时间类型标识
     */
    public static final Integer DAY = 1;
    public static final Integer WEEK = 2;
    public static final Integer MONTH = 3;

    /**
     * 是否收取停车费
     */
    public static final Integer PARKING_FEE_ALLOCABLE = 1;
    public static final Integer PARKING_FEE_ALLOCDISABLE = 2;

    /**
     * 超级管理员
     */
    public static final String SUPER_ADMIN = "admin";


    /**
     * 道闸厂商
     */
    public static final Integer BARRIER_MANUFACTURER_BOOSTED_GOAL = 0;
    public static final Integer BARRIER_MANUFACTURER_KEY_TOP = 1;

    /**
     * 请求头 内容类型
     */
    public static final String APPLICATION_JSON = "application/json;charset=UTF-8";
    public static final String APPLICATION_FORM =  "application/x-www-form-urlencoded; charset=UTF-8";

    /**
     * key top道闸我方给的APPID APP密钥
     */
    public static final Integer KEY_TOP_APPID = 73505907;
    public static final String KEY_TOP_SECRET = "hr9whk6rsv";

    /**
     * 积分常量
     */
    public static final String INTEGRAL_TIME_OUT = "the_overtime_code";
    public static final int INTEGRAL_ADD = 1;
    public static final int INTEGRAL_SUBTRACT = 2;

    public static final Integer INTEGRAL_TYPE_ONE=1;

    public static final Integer INTEGRAL_TYPE_TWO=2;

    /**
     * 阶梯类型 邀请 1  被邀请2
     */
    public static final int LADDER_TYPE_INVITER = 1;
    public static final int LADDER_TYPE_INVITEE = 2;

    /**
     * 车场计费类型0：道闸计费，1：车锁计费
     */
    public static final Integer PARK_LOT_CHARGE_TYPE_ZEOR = 0;
    public static final Integer PARK_LOT_CHARGE_ONE = 1;

    /**
     * B端角色
     */
    public static final String SUPADMIN_ROLE = "SUPADMIN_ROLE"; // 超级管理员
    public static final String ENTERPRISE_ROLE = "ENTERPRISE_ROLE"; // 企业角色
    public static final String CHILD_ROLE = "CHILD_ROLE"; // 子账户角色

    /**
     * 套餐显示类型：0：不展示，1：展示
     */
    public static final Integer PACKAGE_CHARGE_TYPE_ZEOR = 0;
    public static final Integer PACKAGE_CHARGE_TYPE_ONE = 1;

    /**
     * 套餐时间类型：1:每天，2：工作日,3:周末
     */
    public static final Integer TIME_TYPE_ONE = 1;
    public static final Integer TIME_TYPE_TWO = 2;
    public static final Integer TIME_TYPE_THREE = 3;

}
