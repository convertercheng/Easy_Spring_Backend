package com.qhieco.commonrepo;

import com.qhieco.commonentity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 下午4:03
 * <p>
 * 类说明：
 * ${description}
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {

    @Query(value = "select fp.feedbackId from FeedbackProblemB fp  where fp.problemId = ?1")
    List<Integer> findIdsByProblem(Integer problem);

    @Query(value = "select u.id from UserMobile u where u.phone like ?1")
    List<Integer> findIdByPhone(String phone);

    @Query("select p.proIntro from Problem p, FeedbackProblemB f where f.feedbackId = ?1 and p.id=f.problemId")
    List<String> findProblem(Integer id);
}
