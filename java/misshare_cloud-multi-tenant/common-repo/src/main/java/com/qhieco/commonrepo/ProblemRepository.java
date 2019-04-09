package com.qhieco.commonrepo;

import com.qhieco.commonentity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 下午2:51
 * <p>
 *    问题Dao
 *
 */

public interface ProblemRepository extends JpaRepository<Problem,Integer> {

    /**
     * 根据状态查询所有问题
     * @param state
     * @return
     */
    List<Problem> findByState(Integer state);


}
