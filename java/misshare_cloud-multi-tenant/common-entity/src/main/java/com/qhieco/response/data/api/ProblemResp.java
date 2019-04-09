package com.qhieco.response.data.api;

import com.qhieco.commonentity.Problem;
import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳
 * @date 2018年4月13日 上午11:43:44
 * 
 */
@Data
public class ProblemResp {
	private List<Problem> Problems;

}
