package com.qhieco.web.tenant;

import com.qhieco.TenantSupport;
import com.qhieco.util.TenantContext;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-3 上午10:43
 * <p>
 * 类说明：
 * ${description}
 */
@Configuration
public class MultiTenantConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factory, DataSource dataSource, JpaProperties properties) {
//    HibernateSettings settings = new HibernateSettings();
//    settings.ddlAuto("create-drop");
        Map<String, Object> jpaProperties = new HashMap<>(properties.getHibernateProperties(dataSource));
        jpaProperties.put("hibernate.ejb.interceptor", hibernateInterceptor());
        jpaProperties.put("hibernate.show_sql", "true");
        return factory.dataSource(dataSource).packages("com.qhieco").properties(jpaProperties).build();
    }

    @Bean
    public EmptyInterceptor hibernateInterceptor() {
        return new EmptyInterceptor() {

            @Override
            public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
                if (entity instanceof TenantSupport && TenantContext.getCurrentTenant() != null) {
                    log.debug("[save] Updating the entity " + id + " with util information: " + TenantContext.getCurrentTenant());
                    ((TenantSupport) entity).setTenantId(TenantContext.getCurrentTenant());
                }
                return false;
            }

            @Override
            public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
                if (entity instanceof TenantSupport && TenantContext.getCurrentTenant() != null) {
                    log.debug("[delete] Updating the entity " + id + " with util information: " + TenantContext.getCurrentTenant());
                    ((TenantSupport) entity).setTenantId(TenantContext.getCurrentTenant());
                }
            }

            @Override
            public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
                if (entity instanceof TenantSupport && TenantContext.getCurrentTenant() != null) {
                    log.debug("[flush-dirty] Updating the entity " + id + " with util information: " + TenantContext.getCurrentTenant());
                    ((TenantSupport) entity).setTenantId(TenantContext.getCurrentTenant());
                }
                return false;
            }

        };
    }
}
