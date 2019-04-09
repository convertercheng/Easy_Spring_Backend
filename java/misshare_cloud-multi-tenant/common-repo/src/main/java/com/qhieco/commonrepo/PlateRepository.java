package com.qhieco.commonrepo;

import com.qhieco.commonentity.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/5 下午11:03
 * <p>
 * 类说明：
 * Plate数据交互层
 */
public interface PlateRepository extends JpaRepository<Plate, Integer> {
    /**
     *  根据车牌号查询车牌
     * @param number 车牌号
     * @param state 状态
     * @return Plate
     */
    Plate findByNumberAndState(String number, Integer state);

    /**
     * 根据车牌号和状态查询到车牌id
     * @param number 车牌号
     * @param state 状态
     * @return 车牌号id
     */
    @Query("select id from Plate where number = ?1 and state = ?2")
    Integer findByNumber(String number, Integer state);

    /**
     * 根据车牌id查询车牌号
     * @param id 车牌id
     * @param state 状态
     * @return 车牌号码
     */
    @Query("select number from Plate where id = ?1 and state = ?2")
    String findByPlateId(Integer id, Integer state);

    /**
     * 根据车牌id查询车牌信息
     * @param id 车牌id
     * @param state 状态
     * @return 车牌号码
     */
    @Query("select p from Plate p where id = ?1 and state = ?2")
    Plate findPlateByPlateId(Integer id, Integer state);

    @Query("select id from Plate where number = ?1 and state = ?2")
    List<Integer> findPlateByNumber(String number, Integer state);

    @Query("select p.number from Plate p where p.id  in  ?2 and p.state =?1 ")
    List<String> findAllByNumber(Integer state, List<Integer> ids);

    /**
     * 通过ids查询车牌
     * @param ids 车牌ids
     * @return Plate List
     */
    @Query("select p from Plate p where p.id in(:ids)")
    List<Plate> findByIds(@Param("ids") List<Integer> ids);
}
