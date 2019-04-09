package com.qhieco.commonrepo.iot;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午2:30
 * <p>
 * 类说明：
 * 自定义dao　暂时弃用
 */
public class IotRepositoryImpl<T> implements IotBaseRepository<T> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<T> searchByProperty(String field, Object value, Class<T> cls) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(cls);
        Root<T> root = query.from(cls);
        query.where(builder.like(root.get(field), "%" + value + "%"));
        return em.createQuery(query.select(root)).getResultList();
    }

    @Override
    public List<T> hqlExcecute(String hqlStr) {

        Query query = em.createQuery(hqlStr);
        List<T> result = query.getResultList();
        return result;

    }


}
