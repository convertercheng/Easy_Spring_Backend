package com.qhieco.webmapper.SqlBuilder;

import com.qhieco.request.web.ApplyInvoiceRequest;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-2 下午4:29
 * <p>
 * 类说明：
 * ${description}
 */
public class ApplySqlBuilder {

    private static final String PAGE_FIELD =
            "        ai.id, " +
            "        um.phone," +
            "        ai.taxpayer_id taxpayerId," +
            "        ai.title," +
            "        ai.fee," +
            "        ai.type," +
            "        ai.email," +
            "        ai.apply_time applyTime," +
            "        ai.state";

    public static String buildPage(final ApplyInvoiceRequest request){
        SQL sql = new SQL();
        sql.SELECT(PAGE_FIELD);
        where(sql, request);
        sql.ORDER_BY("id desc LIMIT #{start},#{length}");
        return sql.toString();
    }

    public static String buildCount(final ApplyInvoiceRequest request) {
        SQL sql = new SQL();
        sql.SELECT("COUNT(ai.id)");
        where(sql, request);
        return sql.toString();
    }

    private static void where(SQL sql, final ApplyInvoiceRequest request){
        sql.FROM("t_apply_invoice ai");
        sql.LEFT_OUTER_JOIN("t_user_mobile um on um.id = ai.mobile_user_id");
        if(request.getPhone()!=null) {
            sql.WHERE("um.phone LIKE concat('%',#{phone},'%'");
        }
        if(request.getTitle()!=null){
            sql.WHERE("ai.title LIKE concat('%',#{title},'%')");
        }
        if(request.getStartApplyTime()!=null){
            sql.WHERE("ai.apply_time>=#{startApplyTime}");
        }
        if (request.getEndApplyTime()!=null){
            sql.WHERE("ai.apply_time<=#{endApplyTime}");
        }

    }
}
