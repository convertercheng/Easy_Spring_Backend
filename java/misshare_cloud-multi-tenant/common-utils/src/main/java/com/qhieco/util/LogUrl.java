package com.qhieco.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.servlet.http.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * Created by wangyu on 11/18/2017.
 */
@Component
public class LogUrl {
    private static final Map<String, OprationType> DistinctOp = new HashMap<String, OprationType>();
    private static final Map<String, OprationType> CommonOp = new HashMap<String, OprationType>();

    static {
        DistinctOp.put("access/bind", new OprationType("门禁管理--停车场绑定门禁--门禁ID:","accessIdList"));
        DistinctOp.put("access/unbind", new OprationType("门禁管理--停车场解绑门禁--门禁ID:","accessId"));
        DistinctOp.put("activity/delete", new OprationType("活动管理--活动删除--活动ID:","id"));
        DistinctOp.put("advert/delete", new OprationType("广告管理--广告删除--广告ID:","id"));
        DistinctOp.put("auth/admin/bind", new OprationType("管理员管理--管理员绑定角色--管理员ID:","userId"));
        DistinctOp.put("auth/admin/unbind", new OprationType("管理员管理--管理员解绑角色--管理员ID:","userId"));
        DistinctOp.put("auth/role/bind",new OprationType("角色管理--角色分配权限--角色ID:", "roleId"));
        DistinctOp.put("auth/role/unbind", new OprationType("角色管理--角色解绑权限--角色ID:","roleId"));
        DistinctOp.put("gateway/bind",new OprationType("网关管理--网关绑定路由器--网关ID:","gatewayIdList"));
        DistinctOp.put("gateway/unbind",new OprationType("网关管理--网关解绑路由器--网关ID:","gatewayId"));
        DistinctOp.put("lock/unbind/parkloc",new OprationType("车位锁管理--车位锁解绑车位--车位锁ID:","lockId"));
        DistinctOp.put("lock/bind/parkloc",new OprationType("车位锁管理--车位锁绑定车位--车位锁ID:","lockIdList"));
        DistinctOp.put("order/clseOrderWithdraw", new OprationType("订单管理--提现订单关闭--订单id:","id"));
        DistinctOp.put("parkloc/add/batch", new OprationType("车位管理--车位批量添加--小区ID:","parklotId"));
        DistinctOp.put("relaymeter/unbind", new OprationType("继电器管理--继电器解绑小区--继电器ID:","relaymeterIdList"));
        DistinctOp.put("relaymeter/bind", new OprationType("继电器管理--继电器绑定小区--继电器ID:","relaymeterId"));
        DistinctOp.put("register/saveUserRegister", new OprationType("注册来源管理--绑定用户来源标记--来源ID:","registerId"));
        DistinctOp.put("router/unbind", new OprationType("路由器管理--路由器解绑小区--路由器ID:","routerIdList"));
        DistinctOp.put("router/bind", new OprationType("路由器管理--路由器绑定小区--路由器ID:","routerId"));
        DistinctOp.put("tag/message/sendJpushByTagId", new OprationType("标签管理--根据标签推送消息--标签列表:", "tagIds"));
        DistinctOp.put("tag/message/sendJpushByUserId", new OprationType("标签管理--根据用户推送消息--用户列表:", "userIds"));
        DistinctOp.put("update/upload", new OprationType("更新管理--更新上传",null));

        CommonOp.put("access/save",new OprationType("门禁","门禁名", "name"));
        CommonOp.put("activity/saveOrUpdate", new OprationType("活动","活动名称","name"));
        CommonOp.put("advert/saveOrUpdate", new OprationType("广告","跳转链接","href"));
        CommonOp.put("area/save", new OprationType("地区","地区名","name"));
        CommonOp.put("auth/admin/save",new OprationType("管理员","账户名", "username"));
        CommonOp.put("auth/role/save",new OprationType("角色","角色名", "name"));
        CommonOp.put("auth/permission/save",new OprationType("权限","权限名", "name"));
        CommonOp.put("barrier/save",new OprationType("道闸","道闸名称","name"));
        CommonOp.put("buiness/save",new OprationType("商家","商家名称","businessName"));
        CommonOp.put("coupon/save",new OprationType("优惠券","金额", "couponLimit"));
        CommonOp.put("fee/rule/add/parking", new OprationType("停车计费规则","规则名称","name"));
        CommonOp.put("fee/rule/add/reserve", new OprationType("预约计费规则","规则名称","name"));
        CommonOp.put("gateway/save", new OprationType("网关","网关Id","identifier"));
        CommonOp.put("lock/save",new OprationType("车位锁","名称","name"));
        CommonOp.put("parkloc/add",new OprationType("车位","车位编号", "number"));
        CommonOp.put("parklot/add", new OprationType().bindFunction(request ->
                !String.valueOf(0).equals(request.getParameter("parklotId"))? "后台操作--小区管理--小区编辑--Id:"+request.getParameter("parklotId"):"后台操作--小区管理--小区新增--小区名称:"+request.getParameter("name")));
        CommonOp.put("message",new OprationType("消息","内容","content"));
        CommonOp.put("params/saveUpdate",new OprationType("参数","参数", "qhKey"));
        CommonOp.put("register/save", new OprationType("注册来源","来源","source"));
        CommonOp.put("relaymeter/save", new OprationType("继电器","名称","name"));
        CommonOp.put("router/save", new OprationType("路由器","名称","name"));
        CommonOp.put("tag/save",new OprationType("标签","标签名", "name"));

    }

    public static String logAction(String url, HttpServletRequest httpServletRequest) {

        OprationType operation;
        operation = CommonOp.get(url);
        if (operation !=  null) {
            if(operation.logFormat!=null){
                return operation.logFormat.apply(httpServletRequest);
            }
            String desc = "后台操作--";
            String id = httpServletRequest.getParameter("id");
            if (id != null) {
                desc += String.format("%s管理--%s编辑--%sId:%s",
                        operation.getName(), operation.getName(), operation.getName(), id);
            } else {
                desc += String.format("%s管理--%s新增--%s:%s",
                        operation.getName(), operation.getName(),
                        operation.getDesc(), httpServletRequest.getParameter(operation.getAttribute()));
            }
            return (desc);
            }
        OprationType oprationType = DistinctOp.get(url);
        if(oprationType == null) {
            oprationType = reOpration(url,"(\\w*bind\\w*)","(\\w+/shift)");

        }
        if (oprationType != null) {
            String[] values = httpServletRequest.getParameterValues(oprationType.getAttribute());
            if (values == null){
                String attribute = (oprationType.getValue()!=null)?oprationType.getValue():"";
                return ("后台操作--"+oprationType.getDesc()+attribute);
            }
            if (values.length == 1){
                return ("后台操作--"+oprationType.getDesc()+values[0]);
            }else {
                return ("后台操作--"+oprationType.getDesc()+Arrays.deepToString(values));
            }
        }

        return null;
    }

    public static OprationType reOpration(String url, String... regulars){
        Pattern numPattern = compile("/(\\d+)/");

        for (int i=0;i<regulars.length;i++) {
            Pattern bindPattern = compile(regulars[i]);
            Matcher bindMatcher = bindPattern.matcher(url);
            if (bindMatcher.find()) {
                OprationType op =  DistinctOp.get(bindMatcher.group(1));
                Matcher numMatcher = numPattern.matcher(url);
                if (numMatcher.find()){
                    op.setValue(numMatcher.group(1));
                }
                return op;
            }
        }
        return null;
    }

    @Data
    private static class OprationType {

        private String name;

        private String desc;

        private String attribute;

        private String value;

        private Function<HttpServletRequest, String> logFormat;

        private OprationType(){}

        private OprationType(String name, String desc, String attribute) {
            this.name = name;
            this.desc = desc;
            this.attribute = attribute;
        }

        private OprationType(String desc, String attribute) {
            this.desc = desc;
            this.attribute = attribute;
        }

        private OprationType bindFunction( Function<HttpServletRequest, String> logFormat){
            this.logFormat = logFormat;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
