package com.qhieco.constant;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 16:00
 * <p>
 * 类说明：
 */
public class Status {

    /**
     * 公用状态
     */
    public enum Common {
        /**
         * 公用状态
         */
        INVALID(0, "失效"),
        VALID(1, "有效"),
        DELETED(9999, "已删除");

        private Integer value;
        private String msg;

        private static final Map<Integer, String> MSG_LOOKUP = new HashMap<Integer, String>();

        static {
            for(Common e : EnumSet.allOf(Common.class)) {
                MSG_LOOKUP.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return MSG_LOOKUP.get(value);
        }

        Common(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
        public String getMsg(){return msg;}
    }
    /**
     * App后台错误码
     */
    public enum ApiErr {
        /**
         * 系统状态值
         */
        SUCCESS(2000, "请求成功"),
        NETWORK_ERROR(2001, "网络异常"),
        UNKNOWN_ERROR(2002, "未知错误"),
        TIMESTAMP_ERROR(2003, "客户端时间戳错误"),
        VERIFICATION_CODE_GET_EXCESS(2004, "获取验证码次数过于频繁"),
        INSERT_ERROR(2005, "数据库新增或插入错误"),
        NUMBER_RANGE_ERROR(2006, "数字大小错误"),
        THIRD_PARTY_ERROR(2007, "第三方接口调用错误"),
        FILE_SAVE_MODIFY_ERROR(2008, "文件存储或修改错误"),
        APP_ID_ERROR(2009, "appId错误"),
        KEY_ERROR(2010, "key错误"),
        CODE_ERROR(2011, "CODE错误"),
        /**
         * 广告页
         */
        PHONE_TYPE_UNKNOWN(2100, "手机类型未知"),
        PHONE_NUM_ERROR(2101, "手机号码错误"),
        /**
         * 参数类的错误
         */
        PARAMS_VERIFICATION_ERROR(2200, "获取验证码的传入参数错误"),
        PARAMS_PARKLOT_QUERY(2201, "搜索停车场的传入参数错误"),
        PARAMS_PARKLOT_USUAL(2202, "设置常用停车场的传入参数错误"),
        PARAMS_PARKLOT_DETAIL(2203, "查看停车场详情的传入参数错误"),
        PARAMS_USER_LOGIN(2204, "用户登录的传入参数错误"),
        PARAMS_PLATE_ADD(2205, "用户添加车牌的传入参数错误"),
        PARAMS_PLATE_GET(2206, "获取车牌号的传入参数错误"),
        PARAMS_PARKLOT_NEARBY(2207, "查询附近空车位的传入参数错误"),
        PARAMS_ORDER_QUERY(2208, "查询用户订单的传入参数错误"),
        PARAMS_COUPON_QUERY(2209, "查询用户卡券列表的传入参数错误"),
        PARAMS_RESERVE_ENTER(2210, "进入预约页面的传入参数错误"),
        PARAMS_PERSON_CENTER_QUERY(2211, "查询用户个人中心的传入参数错误"),
        PARAMS_PLATE_DEL(2212, "删除车牌号的传入参数错误"),
        PARAMS_INVOICE_QUERY(2213, "查询发票信息的传入参数错误"),
        PARAMS_INVOICE_DETAIL(2214, "查询发票详情的传入参数错误"),
        PARAMS_INVOICE_MAKE(2215, "开具电子发票的传入参数错误"),
        PARAMS_BILL_QUERY(2216, "查询用户账单的传入参数错误"),
        PARAMS_COUPON_EXCHANGE(2217, "兑换优惠券的传入参数错误"),
        PARAMS_PARKLOC_ADD(2218, "添加车位的传入参数错误"),
        PARAMS_USER_WITHDRAW_QUERY(2219, "查询用户可提现金额的传入参数错误"),
        PARAMS_BANKCARD_ADD(2220, "添加银行卡的传入参数错误"),
        PARAMS_INVOICE_ORDERLIST(2221, "查询用户可开发票订单列表的传入参数错误"),
        PARAMS_BANKCARD_UNBIND(2222, "解绑银行卡的传入参数错误"),
        PARAMS_AREAS_SUPPORT_GET(2223, "获取支持地区列表的传入参数错误"),
        PARAMS_PUBLISH_ADD(2224, "业主发布车位的传入参数错误"),
        PARAMS_PUBLISH_CANCEL(2229, "取消发布的传入参数错误"),
        PARAMS_RESERVE_ORDER_DETAIL(2227, "查询预约详情的传入参数错误"),
        PARAMS_RESERVE_ORDER_LIST(2228, "查询预约列表的传入参数错误"),
        PARAMS_UPLOAD_AVATAR(2225, "用户上传头像的传入参数错误"),
        PARAMS_PUBLISH_PARKLOC_QUERY(2229, "查询用户车位发布列表的传入参数错误"),
        PARAMS_WITHDRAW_RECORD_QUERY(2230, "查询用户车位发布列表的传入参数错误"),
        PARAMS_ERROR(2231, "接口传入参数错误"),
        /**
         * 不存在的错误
         */
        NONEXISTENT_PARKLOT(2300, "停车场不存在"),
        NONEXISTENT_USER(2301, "用户不存在"),
        NONEXISTENT_USER_PLATE(2302, "用户车牌信息不存在"),
        NONEXISTENT_ORDER_PARKING(2303, "停车订单不存在"),
        NONEXISTENT_COUPON_CODE(2304, "优惠券code不存在"),
        NONEXISTENT_AREA(2305, "地区不存在"),
        NONEXISTENT_BANKCARD(2306, "银行卡不存在"),
        NONEXISTENT_PARKLOC_USER(2307, "停车位和用户对应关系不存在"),
        NONEXISTENT_BALANCE_USER(2309, "用户账户不存在"),
        NONEXISTENT_BANK_CARD(2310, "用户银行卡不存在"),
        NONEXISTENT_AVATAR_FILE(2308, "用户上传头像的信息为空"),
        NONEXISTENT_SHARE(2310, "共享时间段不存在"),
        NONEXISTENT_FEE_RULE(2311, "收费规则不存在"),
        NONEXISTENT_COUPON(2312, "优惠券不存在"),
        NONEXISTENT_LOCK(2313, "地锁不存在"),
        NONEXISTENT_GATEWAY(2314, "网关不存在"),
        NONEXISTENT_PARKLOT_OF_THE_USER(2315, "该用户名下车场不存在"),
        NONEXISTENT_TAG_EPATIC_WEBHOOK(2316 , "该出入场通知tag不存在"),
        NONEXISTENT_ORDER_RESERVE_PARKING(2317, "不存在此预约订单"),
        NONEXISTENT_USER_JPUSHREGID(2318, "用户极光Id不存在"),
        NONEXISTENT_USER_IP(2319, "用户IP不能为空"),
        NONEXISTENT_USER_MODEL(2320, "用户型号不能为空"),
        NONEXISTENT_WX_MODEL(2321, "openId获取失败"),
        NONEXISTENT_WX_USER(2322, "手机号码已被占用"),
        NONEXISTENT_WX_OPENID(2323, "微信号已被占用"),
        PLATE_NAME_EXISTS(2324, "该车牌已绑定其他用户"),
        PLATE_NAME_TYPE(2325, "管理员不能预约"),
        NONEXISTENT_PARKING_ORDER(2326, "未找到对应的停车订单"),
        ADMIN_NAME_TYPE(2327, "管理员不能登录"),
        NONEXISTENT_PARKLOC(2328, "停车位不存在"),
        /**
         * 第三方账号错误
         */
        ERROR_JPUSH_ID(2400, "极光推送id格式错误"),
        /**
         * 重复错误
         */
        REPEAT_ADD_PLATE(2500, "用户已经添加该车牌"),
        REPEAT_MAKE_INVOICE(2501, "订单已经开票"),
        REPEAT_COUPON_EXCHANGE(2502, "优惠券已经被兑换"),
        REPEAT_BANKCARD_ADD(2503, "用户已经绑定过银行卡"),
        REPEAT_PUBLISH_TIME(2504, "用户发布车位的时间重复"),
        REPEAT_ORDER_CANCEL(2505, "订单重复取消"),
        REPEAT_PARKING(2506, "订单重复停车"),
        REPEAT_LEAVE(2507, "订单重复出场"),
        /**
         * 发票错误
         */
        FAIL_MAKE_INVOICE (2600, "开票失败"),
        /**
         * 不匹配错误
         */
        MISMATCH_UNBIND_BANKCARD(2700, "所传持卡人与银行卡不匹配"),
        MISMATCH_VERIFYBANKCARD4(2701, "聚合银行卡四元素验证不匹配"),
        MISMATCH_PLATE(2702, "离场和入场车牌不匹配"),
        /**
         * 时间错误
         */
        PUBLISH_TIME_ILLEGAL(2800, "发布时间非法"),
        RESERVE_TIME_ILLEGAL(2801, "入场时间已过，请重新选择"),
        TIME_ILLEGAL(2802, "时间转化错误"),
        /**
         * 预约错误
         */
        EXIST_UNFINISHED_ORDER(2900,"存在未完成的订单"),
        PARKLOC_CANT_RESERVE(2901,"车位不能被预约"),
        NOT_PAY_ORDER(2902,"订单当前不是支付的状态"),
        ORDER_ALREADY_PARKING(2903,"订单已经停车不能取消"),
        EXIST_UNFINISHED_PLATE_ORDER(2904,"该车辆已存在预约订单"),
        REPEAT_SHARE_RESERVE(2905,"该时间段已被预约"),
        REPEAT_WHITE_LIST(2906,"您暂无预约资格，请联系物业管理员"),
        PARKLOC_CANT_CANCEL(2907,"控制车锁时不能取消预约"),
        EMPTY_PARKLOC(2908, "该时间段没有可预约的空车位，请重新预约"),
        EMPTY_DISTINCT_PARKLOC(2909, "当前区域没有可预约的车位"),
        EMPTY_RESERVE_PARKLOC(2910, "当前车位不可预约"),
        /**
         * 当前版本不支持错误
         */
        NOT_SUPPORT_ONE_ADMIN_TO_MANY_PARKLOTS(3001, "暂时不支持管理员拥有多个车场"),

        PACKAGE_ERROR(2911, "套餐数量已上限");

        private Integer code;
        private String msg;

        ApiErr(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
    /**
     * Web后台错误码
     */
    public enum WebErr {
        /**
         * 系统状态值
         */
        SUCCESS(2000, "请求成功"),
        SYSTEM_ERROR(2001, "系统出错"),
        INSERT_ERROR(2002, "数据库新增或更新错误"),
        PARAM_ERROR(2003, "请求传入参数错误"),
        FILE_TOOBIG_ERROR(2004, "上传文件过大"),
        /**
         * 参数类的错误
         */
        PARAMS_USERNAME_OR_PASSWORD_ERROR(2100, "用户名或密码错误"),
        PARAMS_PARKLOT_ID_BIND_PARKLOC(2101, "静态车场不可绑定车位"),
        PARAMS_PARKLOT_USER_BIND_PARKLOC(2102, "约车场与管理员不匹配"),
        PARAMS_PARKLOT_ADMIN_ERROR(2103, "该车场不存在管理员"),
        PARAMS_PARKLOC_COUNT_ERROR(2104, "新增车位数不能大于签约车位数"),
        PARAMS_USERPASS_CHECK_ERROR(2105, "两次密码输入不一致"),
        /**
         * 传入参数为空错误
         */
        EMPTY_PARKLOT_ID(2200, "添加车位未传入停车区id"),
        ENTITY_NOT_EXISTS(2201, "对应Id的数据不存在"),
        EMPTY_EXCEL_PARAM(2202, "excel导出参数不可全为空"),
        EMPTY_INPUT_PARAM(2203, "参数或参数列表为空"),
        EMPTY_AREA_GET_PARAM(2204, "获取地区接口的参数为空"),
        EMPTY_PARKLOT_ADD_PARAM(2205, "添加新停车区的参数为空"),
        EMPTY_FEE_RULE_RESERVE_ADD_PARAM(2206, "添加预约费用规则的参数为空"),
        EMPTY_FEE_RULE_PARKING_ADD_PARAM(2207, "添加停车费用规则的参数为空"),
        EMPTY_FEE_RULE_BIND(2208, "绑定费用规则的参数为空"),
        /**
         * 重复错误
         */
        USERNAME_EXISTS(2300, "用户名已存在"),
        DISTRICT_EXISTS(2301, "地区已存在"),
        ESTATE_EXISTS(2302, "小区已存在"),
        PARKING_NAME_EXISTS(2303, "车位名称已存在"),
        ACCESS_HAS_BINDED(2304, "门禁已绑定"),
        ROLE_NAME_EXISTS(2305, "角色名称已存在"),
        BUSINESS_EXISTS(2306, "商家名称已存在"),
        FEE_RULE_NAME_EXISTS(2307, "费用规则名称已存在"),
        PROPERTY_EXISTS(2310, "属性已存在"),
        RELAYMETER_EXISTS(2311, "继电器名称已存在"),
        TAG_NAME_EXISTS(2312, "标签名称已存在"),
        PARKLOT_LNGLAT_DUPLICATE(2313, "新增或者修改小区的经纬度重复，保存失败"),
        LOCK_BTNAME_BTMAC_DULICATE(2314, "新增或者修改车锁的蓝牙名称或者Mac地址重复，保存失败"),
        DATA_DUPLICATE(2315, "数据重复"),
        RED_INVOICE_DUPLICATE(2316, "发票已经被冲红"),
        PHONE_NUM_EXISTS(2317, "号码已经存在"),
        PRIZE_NAME_REPEAT(2318, "已存在同名奖品"),
        ACTIVITY_DATA_DUPLICATE(2319, "活动时间内，已存在同类型活动"),
        FEE_RULE_PARKING_TIME_DUPLICATE(2320, "计费时段重复，请重新提交"),
        DISTRICT_NAME_EXISTS(2321, "区域名称已经存在"),

        /**
         * 黑名单类的错误
         */
        FORBIDDEN_USER(2400, "用户已禁止"),
        /**
         * 用户角色类错误
         */
        OWNER_CANNOT_BE_AN_ADMINISTRATOR(2500, "该号码已经是其它小区管理员"),
        USER_WITH_AVAILABLE_BALANCE_CANNOT_BE_AN_ADMINISTRATOR(2501, "该车主账户剩余可用金额，不可修改成管理员身份"),
        USER_WITH_AVAILABLE_INVOICE_CANNOT_BE_AN_ADMINISTRATOR(2502, "该车主账户存在可开票金额，不可修改成管理员身份"),
        USER_WITH_ONGOING_ORDERS_CANNOT_BE_AN_ADMINISTRATOR(2503, "该车主存在进行中订单，不可修改成管理员身份"),
        /**
         * 数量错误
         */
        SIGNED_AMOUNT_CANNOT_EXCEED_TOTAL_AMOUNT(2600, "签约车位数量不能超过总车位数量"),
        SIGNED_AMOUNT_CANNOT_LESS_THAN_PUBLISHED_AMOUNT(2601, "签约车位数量不能少于已发布车位数量"),
        MORE_LOCK_BELONG_TO_ONE_PARKLOC(2602, "同一车位不可绑定多个车锁"),
        FEE_RULE_PARKING_COUNT_TIME_ERROR(2603, "所有时段之和最大为24小时"),
        /**
         * 第三方工具错误
         */
        INVALID_SMS_TEMPLATE(2700, "未按照模板推送短信"),
        EMPTY_EXCEL(2701, "excel导出结果为空"),
        TRAFFIC_MANAGE_EEROR(2710,"交通管理平台请求错误"),
        TRAFFIC_MANAGE_UPLOAD_FAILD(2711, "华为云图片上传错误"),
        /**
         * 会导致空指针的错误
         */
        ILLEGAL_PARKLOC_ID(2800, "停车订单表有非法停车位id"),
        ILLEGAL_MOBILE_USER_ID(2801, "停车订单表有非法用户id"),
        ILLEGAL_PARKLOT_ID(2802, "车位表有非法停车区id"),
        ILLEGAL_MOBILE_USER_ID_IN_PARKLOC(2803, "车位表有非法用户id"),
        ILLEGAL_ORDER_PARKING_ID(2804, "停车订单表有非法id"),
        ILLEGAL_RESERVATION_ID_IN_ORDER_PARKING(2805, "停车订单表有非法预约id"),
        ILLEGAL_PLATE_ID_IN_RESERVATION(2806, "预约表有非法车牌id"),
        ILLEGAL_FEE_TYPE(2807, "费用参数传入非法类型"),
        ILLEGAL_LOCK_ID(2808, "锁id类型非法"),
        ILLEGAL_ALL(2809, "非法id参数"),

        /**
         * 当前版本不支持错误
         */
        NOT_SUPPORT_ONE_ADMIN_TO_MANY_PARKLOTS(3001, "暂时不支持管理员拥有多个车场"),
        /**
         * 权限类错误
         */
        WRONG_NAME_PASSWORD(2900,"用户名或密码输入错误，登录失败!"),
        LOGIN_FAILD(2901,"登录失败"),
        LOGIN_REQUIRE(2902,"需要登录认证"),
        ACCESS_DENIED(2903,"无权限访问该页面"),


        PRIZE_DEL_ERROR_ACTIVITY_VALID(3100, "奖品尚在有效期内或者该道具已配置为活动奖励，如需删除，请先修改活动配置。"),
        PRIZE_DEL_ERROR_USER_VALID(3101, "奖品已有用户领取且尚未过期"),
        PRIZE_FROZEN_ERR_TIMEOUT(3102, "奖品已过期，不能冻结"),
        PRIZE_UNFREEZE_ERR_TIMEOUT(3103, "奖品已过期，不能解冻"),
        PRIZE_UNFREEZE_ERR_STATE(3104, "该奖品状态不能解冻"),

        /**
         * 导报表类异常
         */
        EXCEL_DATA_TOOBIG(3200, "导出数据量超过5000条，请重新选择导出条件。"),
        ;



        private int code;
        private String msg;

        WebErr(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * B端Web后台
     */
    public enum BItemErr{
        /**
         * 系统状态值
         */
        SUCCESS(2000, "请求成功"),
        SYSTEM_ERROR(2001, "系统出错"),
        INSERT_ERROR(2002, "数据库新增或更新错误"),
        PARAM_ERROR(2003, "请求传入参数错误"),
        FILE_TOOBIG_ERROR(2004, "上传文件过大"),
        /**
         * 参数类的错误
         */
        PARAMS_USERNAME_OR_PASSWORD_ERROR(2100, "用户名或密码错误"),
        PARAMS_USERPASS_CHECK_ERROR(2105, "两次密码输入不一致"),
        /**
         * 传入参数为空错误
         */
        ENTITY_NOT_EXISTS(2201, "对应Id的数据不存在"),
        EMPTY_EXCEL_PARAM(2202, "excel导出参数不可全为空"),
        EMPTY_INPUT_PARAM(2203, "参数或参数列表为空"),
        /**
         * 重复错误
         */
        USERNAME_EXISTS(2300, "用户名已存在"),
        DATA_DUPLICATE(2301, "数据重复"),
        ACCOUNT_TYPE_CAROWNER(2310, "该账户已经是业主身份，不可修改成管理员身份"),
        ACCOUNT_TYPE_CAROWNER_RECEIPT_MONEY(2311, "该账户存在可开票余额，不可修改成管理员身份"),
        ACCOUNT_TYPE_CAROWNER_EXISTS_ORDER(2312, "该车主存在进行中订单，不可修改成管理员身份"),
        ACCOUNT_TYPE_CAROWNER_EXISTS(2313, "该账户已经是其它小区管理员");

        private int code;
        private String msg;

        BItemErr(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 支付渠道
     */
    public enum Channel{
        UNPAY(0,"未支付费用"),
        ZHIFUBAO(1,"支付宝"),
        WEIXIN(2,"微信支付"),
        CASH(3,"现金"),
        COUPON(4,"优惠券抵扣"),
        PLATFORM(5,"微信支付"),
        PACKAGE(6,"优惠套餐");

        private Integer value;
        private String msg;

        Channel(Integer value, String msg){
            this.value = value;
            this.msg= msg;
        }

        public static final Map<Integer, String> LOOKUP = new HashMap<>();

        static {
            EnumSet.allOf(Channel.class).forEach(channel -> LOOKUP.put(channel.value,channel.msg));
        }

        public static String find(Integer value){return LOOKUP.get(value);}
        public Integer getValue(){return value;}
    }

    /**
     * 车位状态
     */
    public enum Parkloc {
        /**
         * 3个状态
         */
        UNPUBLISHED(1100, "未发布，默认状态"),
        PUBLISHED(1101, "已发布"),
        RESERVED(1102, "已预约");

        private Integer value;
        private String msg;

        Parkloc(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }

    /**
     * 车锁类型
     */
    public enum Lock {
        /**
         * 公用状态
         */
        LOCKZIGB(0, "zigbee锁"),
        LOCKNB(1, "nb锁"),
        LOCK_BT(2, "蓝牙锁"),
        LOCK_LORA(3, "Lora锁");

        private Integer value;
        private String msg;

        private static final Map<Integer, String> MSG_LOOKUPS = new HashMap<Integer, String>();

        static {
            for(Lock e : EnumSet.allOf(Lock.class)) {
                MSG_LOOKUPS.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return MSG_LOOKUPS.get(value);
        }

        Lock(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
        public String getMsg(){return msg;}
    }


    /**
     * 用户类型
     */
    public enum userType {
        USERTYPE_ONE(1,"业主"), USERTYPE_ZEOR(0, "车主"), USERTYPE_TWO(2, "管理员");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(userType e : EnumSet.allOf(userType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        userType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 小区类型
     */
    public enum ParklotType {
        STATIC_PARK(0,"车位查询"), YUE_CHE_CHANG(1, "约车场"), YUE_CHE_WEI(2, "约车位");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(ParklotType e : EnumSet.allOf(ParklotType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        ParklotType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 停车订单状态
     */
    public enum OrderParking{
        UNCONFIRMED(1300, "车位预留(未支付停车费)"),
        RESERVED(1301, "已预约"),
        USED(1302, "已停车"),
        UNPAID(1303, "已离开未支付"),
        PAID(1304, "已支付"),
        SYS_CANCELED(1307, "系统取消"),
        TIMEOUT(1308, "超时取消"),
        CUST_SERVICE_CANCELED(1310, "客服取消"),
        USER_CANCELED(1309, "客户取消");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(OrderParking e : EnumSet.allOf(OrderParking.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        OrderParking(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }
    /**
     * 退款订单状态
     */
    public enum Refund {
        /**
         * 4个状态
         */
        PROCESSING(1500, "退款中"),
        PROCESS_SUCCESS_TOTAL(1501, "全部退款成功"),
        PROCESS_SUCCESS_PARTIAL(1502, "部分退款成功"),
        PROCESS_FAILED(1503, "退款失败");
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();
        private Integer value;
        private String msg;

        static {
            for(Refund e : EnumSet.allOf(Refund.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        Refund(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }
    /**
     * 提现订单状态
     */
    public enum Withdraw {
        /**
         * 3个状态
         */
        PROCESSING(1600, "提现中"),
        PROCESS_SUCCESS(1601, "提现成功"),
        PROCESS_FAILED(1602, "提现失败");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(Withdraw e : EnumSet.allOf(Withdraw.class)) {
                lookup.put(e.value, e.msg);
            }
        }
        public static String find(Integer value) {
            return lookup.get(value);
        }

        Withdraw(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }
    /**
     * 退款渠道
     */
    public enum RefundChannel {
        /**
         * 3个状态
         */
        CHANNELONE(1,"支付宝"),
        CHANNELTWO(2,"微信"),
        CHANNELTHREE(3,"优惠券抵扣"),
        CHANNELFOUR(4,"现金支付"),
        CHANNELFIVE(5,"微信"),
        CHANNELSIX(6,"微信");
//        CHANNEONE(1800, "支付宝"),
//        CHANNETWO(1801, "微信"),
//        CHANNESAN(1803, "微信支付宝");


        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(RefundChannel e : EnumSet.allOf(RefundChannel.class)) {
                lookup.put(e.value, e.msg);
            }
        }
        public static String find(Integer value) {
            return lookup.get(value);
        }

        RefundChannel(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }
    /**
     * 优惠券状态
     */
    public enum Coupon {
        /**
         * 2个状态
         */
        USED(1900, "优惠券已使用"),
        UNCLAIMED(1901, "优惠券未领取"),
        COUPON_CONVERTIBILITY(1902, "优惠券已兑换"),
        COUPON_EXPIRE(1903, "优惠券已过期"),
        COUPON_CONEN_TYPE_ONE(1,"优惠券");
        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(Coupon e : EnumSet.allOf(Coupon.class)) {
                lookup.put(e.value, e.msg);
            }
        }
        public static String find(Integer value) {
            return lookup.get(value);
        }
        Coupon(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }
    /**
     * 工单申请状态
     */
    public enum Apply {
        /**
         * 三个状态
         */
        PROCESSING(1700, "未处理"),
        PROCESS_SUCCESS(1701, "通过"),
        PROCESS_FAILED(1702, "已拒绝"),
        PROCESS_RED(1703, "已冲红");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(Apply e : EnumSet.allOf(Apply.class)) {
                lookup.put(e.value, e.msg);
            }
        }
        public static String find(Integer value) {
            return lookup.get(value);
        }

        Apply(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }

    /**
     * 发票类型
     */
    public enum ApplyType {
        /**
         * 三个状态
         */
        PROCESSING(1, "企业发票"),
        PROCESS_SUCCESS(2, "个人发票");
        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for(ApplyType e : EnumSet.allOf(ApplyType.class)) {
                lookup.put(e.value, e.msg);
            }
        }
        public static String find(Integer value) {
            return lookup.get(value);
        }

        ApplyType(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
    }

    /**
     * 发布状态
     */
    public enum Publish{
        /**
         * 1800待取消（车位空出来之后变为取消）
         */

        TOBECANCELLED(1800, "待取消"),
        TOBEALTER(1801, "待修改");

        private int value;
        Publish(Integer value, String msg){
            this.value = value;
        }
        public Integer getInt(){
            return this.value;
        }
    }

    public enum PublishMode{
        /**
         * 单次发布车位
         */
        ONCE(0, "一次发布"),
        LOOP(1, "循环发布");

        private int value;
        PublishMode(Integer value, String msg) {
            this.value = value;
        }
        public Integer getInt() {
            return this.value;
        }
    }

    /**
     * 收费类型
     */
    public enum FeeType{
        /**
         * 0:担保费，1:预约费，2:停车费
         */
        GUARANTEE(0, "担保费"),
        RESERVE(1, "预约费"),
        PARKING(2, "停车费");

        private int value;
        private FeeType(int value, String msg){
            this.value = value;
        }
        public Integer getInt(){
            return this.value;
        }
    }

    public enum OrderType {
        TYPE_RESERVE(0, "预约订单"),
        TYPE_PARKING(1, "停车订单"),
        TYPE_REFUND(2, "退款订单"),
        TYPE_CHARGE(3, "充值订单"),
        TYPE_WITHDRAW(4, "提现订单");

        private int value;

        private OrderType(int value, String msg) {
            this.value = value;
        }

        public int getInt() {
            return this.value;
        }
    }

    public enum RelayType {
        ENTER(0, "入口"),
        LEAVE(1, "出口");
        private Integer value;
        private String msg;

        private static final Map<Integer, String> MSG_LOOKUP = new HashMap<Integer, String>();

        static {
            for(RelayType e : EnumSet.allOf(RelayType.class)) {
                MSG_LOOKUP.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return MSG_LOOKUP.get(value);
        }

        RelayType(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return value;
        }
        public String getMsg(){return msg;}
    }

    public enum InvoiceStatus {
        UNMAKE(2000, "未开票"),
        MAKING(2001, "开票中"),
        MAKED_SUCCESS(2002, "开票成功"),
        MAKED_FAILED(2003, "开票失败");

        private int value;
        private String messge;
        InvoiceStatus(int value, String msg) {
            this.value = value;
            this.messge = msg;
        }
        public int getInt() {
            return this.value;
        }
    }

    public enum Business{
        BUSINESS_STATUS_NORMAL(1,"正常"),
        BUSINESS_STATUS_END(0,"终止");
        private int value;
        private String messge;
        Business(int value, String msg) {
            this.value = value;
            this.messge = msg;
        }
        public int getInt() {
            return this.value;
        }
    }

    public enum LogOperateType {
        /**
         * 用户日志操作类型，t_log_operation_mobile的type值
         */
        TYPE_LOGIN(1, "登录操作"),
        TYPE_RESERVE(2, "预约"),
        TYPE_PARKING(3, "停车"),
        TYPE_PAY(4, "支付"),
        TYPE_REFUND(5, "退款"),
        TYPE_WITHDRAW(6, "提现"),
        TYPE_INVOICE(7, "开票"),
        TYPE_MESSAGE(8, "查看消息");

        private int value;
        private String msg;

        LogOperateType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public int getInt(){
            return this.value;
        }
    }

    public enum ParklocNum{
        /**
         * 显示车位数
         */
        SHOW(1,"显示"),
        /**
         * 不显示车位数
         */
        HIDDEN(0,"不显示");

        private int value;
        private String messge;
        ParklocNum(int value, String msg) {
            this.value = value;
            this.messge = msg;
        }
        public int getInt() {
            return this.value;
        }
    }

    public enum ActivityFileState {
        // 活动图片类型
        INDEX(1, "首页banner图:长图"),
        LIST(2, "活动列表banner图：宽图");

        private int value;
        private String msg;

        ActivityFileState(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public int getInt(){
            return this.value;
        }
    }

    /**
     * 停车场类型
     */
    public enum ParkingKind {
        INNER(0, "室内"),
        OUTER(1, "室外"),
        INANDOUT(2, "室内＋室外");
        private Integer value;
        private String msg;

        private static final Map<Integer, String> MSG_LOOKUP = new HashMap<Integer, String>();

        static {
            for(ParkingKind e : EnumSet.allOf(ParkingKind.class)) {
                MSG_LOOKUP.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return MSG_LOOKUP.get(value);
        }

        ParkingKind(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }
    /**
     * 停车场是否共享
     */
    public enum ParkingInner {
        INNERZROR(0, "普通"),
        INNERONE(1, "内部共享");
        private Integer value;
        private String msg;

        private static final Map<Integer, String> MSG_LOOKUPS = new HashMap<Integer, String>();

        static {
            for(ParkingInner e : EnumSet.allOf(ParkingInner.class)) {
                MSG_LOOKUPS.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return MSG_LOOKUPS.get(value);
        }

        ParkingInner(Integer value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }
    /**
     * 博思高提示语
     */
    public enum BSGMessage {
        NORMAL(0, "正常"), NOT_FIND(1, "找不到入场记录"), NO_NEED_PAY(2, "不需要缴费"), NO_SUPPORT_PHONE(3, "不支持手机缴费"),
        NETWORK_ANOMALY(4, "网络异常"), OTHER_ANOMALY(5, "其他异常"), NON_TEMPORARY_CARD(6, "非临时卡"), NOT_FIND_CARD(7, "找不到卡片"),
        REPEAT_PAY(10, "重复缴费"), MOENY_ERROR(11, "金额不对"), ORDER_INVALID(12, "订单失效");

        private Integer value;
        private String msg;
        private static final Map<Integer, String> lookup = new HashMap<Integer, String>();

        static {
            for (BSGMessage e : EnumSet.allOf(BSGMessage.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        BSGMessage(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum Jumpable {
        //  是否可跳 0可跳，1不可跳
        JUMPABLE(0, "可跳"), UNJUMABLE(1, "不可跳");
        private int value;
        private String msg;

        Jumpable(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (Jumpable e : EnumSet.allOf(Jumpable.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    public enum PhoneType {
        //  适配的手机类型 0：安卓 1：iOS
        ANDROID(0, "安卓"), IOS(1, "IOS");
        private int value;
        private String msg;

        PhoneType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (PhoneType e : EnumSet.allOf(PhoneType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    public enum ActivityType {
        //  活动类型 1 邀请
        INVITE(1, "邀请"),
        REGISTE(2, "注册"),
        FIRST_ORDER(3, "首次下单"),
        BINDING_PLATE(4, "绑车牌");
        private int value;
        private String msg;

        ActivityType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (ActivityType e : EnumSet.allOf(ActivityType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
        public int getInt(){
            return this.value;
        }
    }

    public enum TriggerType {
        //  触发条件类型 1 邀请
        INVITE(1, "邀请"),
        REGISTE(2, "注册"),
        FIRST_ORDER(3, "首次下单"),
        BINDING_PLATE(4, "绑车牌");
        private int value;
        private String msg;

        TriggerType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (TriggerType e : EnumSet.allOf(TriggerType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }
    public enum PrizeType {
        //  奖品类型 1 优惠券
        COUPON(1, "优惠券");
        private int value;
        private String msg;

        PrizeType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();
        static {
            for (PrizeType e : EnumSet.allOf(PrizeType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }
    public enum PrizeState {
        //  奖品状态
        FROZEN(9998, "奖品冻结");
        private int value;
        private String msg;

        PrizeState(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getInt() {
            return this.value;
        }
    }

    public enum LaType {
        INVITE(1, "邀请"), INVITED(2, "被邀请");
        private int value;
        private String msg;

        LaType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (LaType e : EnumSet.allOf(LaType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    /**
     * 状态(1已上架,2已下架,3待上架,4已满额)
     */
    public enum PackageState  {
        UP(1, "已上架"),
        OFF(2, "已下架"),
        WAIT(3, "待上架"),
        FULL(4, "已满额");
        private int value;
        private String msg;

        PackageState(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (PackageState e : EnumSet.allOf(PackageState.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    /**
     * 停车类
     */
    public enum PackageType  {
        STOPCAL(1, "停车类");
        private int value;
        private String msg;

        PackageType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (PackageType e : EnumSet.allOf(PackageType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    /**
     * 小区绑定套餐展示状态
     */
    public enum PackFlot  {
        NOTSHOW(0, "不展示"),
        SHOW(1, "展示");
        private int value;
        private String msg;

        PackFlot(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (PackFlot e : EnumSet.allOf(PackFlot.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    /**
     * 时段类型(每天，工作日/周末)
     */
    public enum RuleTimeTypeStr  {
        EVERYDAY(1, "每天"),
        WEEKEND(2, "工作日"),
        WEEKENDT(3, "周末");
        private int value;
        private String msg;

        RuleTimeTypeStr(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (RuleTimeTypeStr e : EnumSet.allOf(RuleTimeTypeStr.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    public enum TagType {
        //  标签类型
        AUTOMATIC(0, "自动标签"),
        MANUAL(1, "手动标签");
        private int value;
        private String msg;

        TagType(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (TagType e : EnumSet.allOf(TagType.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }

        public Integer getInt() {
            return this.value;
        }
    }

    public enum PaidFlag {
        OFF_LINE_UNPAY(0, "线下未支付"),
        OFF_LINE_PAID(1, "线下已支付");

        private Integer value;
        private String msg;

        PaidFlag(int valud, String msg) {
            this.value = valud;
            this.msg = msg;
        }

        public Integer getInt(){
            return this.value;
        }

        private static final Map<Integer, String> lookup = new HashMap<>();

        static {
            for (PaidFlag e : EnumSet.allOf(PaidFlag.class)) {
                lookup.put(e.value, e.msg);
            }
        }

        public static String find(Integer value) {
            return lookup.get(value);
        }
    }

    /**
     * userWeb分级
     */
    public enum UserWebLevel {
        PLATFORM(1, "平台管理员"),
        COMPANY(2, "企业父账户"),
        SUB_ACCOUNT(3, "子账户");

        @Getter
        private Integer value;
        private String msg;

        UserWebLevel(Integer value, String msg) {
            this.msg = msg;
            this.value = value;
        }
    }
}
