package com.jaswine.gateway.manage.bean.dto;

import com.jaswine.bean.route.RedisFilterDefinition;
import com.jaswine.bean.route.RedisPredicateDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Jaswine
 * @date : 2020-07-09 09:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RouteDTO {


	/** 路由id */
	private String routeId;
	/** 指向 */
	private String uri;
	/** 谓语动词 */
	private List<RedisPredicateDefinition> predicates;
	/** 过滤器 */
	private List<RedisFilterDefinition> filters;
	/** 顺序 */
	private Integer orders;
	/** 描述 */
	private String description;
	/** 状态 */
	private Integer status;
}
