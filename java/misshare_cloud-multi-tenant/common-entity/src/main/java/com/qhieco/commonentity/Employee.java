package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Employee implements TenantSupport {
  @Id
  @GeneratedValue
  private Integer id;

  private String firstName;

  private String lastName;

  private Integer tenantId;


  @Transient
  private String tenantName;
}
