package com.qhieco.util;

public class TenantContext {

  private static ThreadLocal<Integer> currentTenant = new ThreadLocal<>();

  public static Integer getCurrentTenant() {
    return currentTenant.get();
  }

  public static void setCurrentTenant(Integer tenant) {
    currentTenant.set(tenant);
  }

  public static void clear() {
    currentTenant.set(null);
  }

}