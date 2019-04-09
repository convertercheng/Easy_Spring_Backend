package com.qhieco;

public interface TenantSupport {
    Integer getTenantId();

    void setTenantId(Integer tenantId);

    String getTenantName();

    void setTenantName(String tenantName);
}
