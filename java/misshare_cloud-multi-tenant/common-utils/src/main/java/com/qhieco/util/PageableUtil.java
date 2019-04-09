package com.qhieco.util;

import com.qhieco.commonentity.Parklot;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yekk modified at 01/11/2018
 */
public class PageableUtil {

	public Root<?> root;
	public CriteriaBuilder cb;
	public List<Predicate> predicates;
	public CriteriaQuery<?> query;

	public PageableUtil(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		this.cb = cb;
		this.predicates = new ArrayList<>();
		this.root = root;
		this.query = query;
	}

	public void equal(String key, Integer value) {
		if(value != null) {
			predicates.add(cb.equal(root.get(key), value));
		}
	}

	public void like(String key, String value) {
		if(value != null && !value.equals("")) {
			predicates.add(cb.like(root.get(key), "%" + value + "%"));
		}
	}

	public void between(String key, Number start, Number end) {
		if(start != null) {
			predicates.add(cb.ge(root.get(key), start));
		}
		if(end != null) {
			predicates.add(cb.le(root.get(key), end));
		}
	}

	public void in(String key, List<Integer> idList){
		if ( idList!=null && idList.size() > 0) {
			predicates.add(root.<Integer>get(key).in(idList));
		}else {
			predicates.add(root.<Integer>get(key).in(-1));
		}
	}

	public void notIn(String key, List<Integer> idList){
		if ( idList!=null && idList.size() > 0) {
			predicates.add(root.<Integer>get(key).in(idList).not());
		}
	}

	public void notequal(String key){
		predicates.add(cb.isNotNull(root.get(key)));
	}

	public void isNull(String key){
		predicates.add(cb.isNull(root.get(key)));
	}

	public   void lessThanOrEqual(CriteriaBuilder cb,String key,BigDecimal value){
		predicates.add(cb.lessThanOrEqualTo(root.get(key), value));
	}
	public   void greaterThanEqual(CriteriaBuilder cb,String key,BigDecimal value){
		predicates.add(cb.greaterThanOrEqualTo(root.get(key), value));
	}

    public   void lessThanOrEqual(CriteriaBuilder cb,String key,Long value){
        predicates.add(cb.lessThanOrEqualTo(root.get(key),value));
    }
    public   void greaterThanEqual(CriteriaBuilder cb,String key,Long value){
        predicates.add(cb.greaterThanOrEqualTo(root.get(key), value));
    }

	public   void notBetween(CriteriaBuilder cb,String key1,String key2,Long value){
		cb.lessThanOrEqualTo(root.get(key1),value);
		predicates.add(cb.or(cb.greaterThanOrEqualTo(root.get(key2), value)));
	}

	public Predicate pridect(){
		return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
	}


	public static PageRequest pageable(Integer sEcho, Integer start, Integer length) {
		start = start < 1 ? 1 : start;
		Integer page = start / length;
		Sort sort = new Sort(Direction.DESC, "id");
		return new PageRequest(page, length, sort);
	}
	
	public static void equal(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, Integer value) {
		if(value != null) {
			predicates.add(cb.equal(root.get(key), value));
		}
	}
	
	public static void equal(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, String value) {
		if(value != null && !"".equals(value)) {
			predicates.add(cb.equal(root.get(key), value));
		}
	}

	public static void like(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, String value) {
		if(value != null && !value.equals("")) {
			predicates.add(cb.like(root.get(key), "%" + value + "%"));
		}
	}
	
	public static void between(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, Number start, Number end) {
		if(start != null) {
			predicates.add(cb.ge(root.get(key), start));
		}
		if(end != null) {
			predicates.add(cb.le(root.get(key), end));
		}
	}
	
	public static void not(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, Integer value) {
		if(value != null) {
			predicates.add(cb.notEqual(root.get(key), value));
		}
	}
	
	public static void not(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key, String value) {
		if(value != null) {
			predicates.add(cb.notEqual(root.get(key), value));
		}
	}
	
	public static void notNull(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key) {
		predicates.add(cb.isNotNull(root.get(key)));
	}

	public static void isNull(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, String key) {
		predicates.add(cb.isNull(root.get(key)));
	}
}
