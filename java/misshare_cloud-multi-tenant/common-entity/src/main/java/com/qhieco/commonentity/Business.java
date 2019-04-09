package com.qhieco.commonentity;

import com.qhieco.commonentity.iotdevice.AbstractIotDevice;
import com.qhieco.request.web.QueryPaged;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 9:48
 * <p>
 * 类说明：
 * 商家信息类
 */
@Entity
@Table(name = "t_business")
@Data
public class Business{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_description")
    private String businessDescription;

    @Column(name = "state")
    private Integer state;

    @Column(name = "modify_time")
    private Long modifyTime;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name="businessStatus")
    private Integer businessStatus;

//    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
//    mappedBy="business" )
//    private List<Coupon> coupon;

    public Business(){}

    public Business(String businessName, String businessDescription, Integer state, Long modifyTime,
                    Long createTime,Integer businessStatus) {
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.state = state;
        this.modifyTime = modifyTime;
        this.createTime=createTime;
        this.businessStatus=businessStatus;
    }

}
