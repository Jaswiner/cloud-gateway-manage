package com.jaswine.gateway.manage.bean.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.jaswine.bean.base.BasePojo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由表
 *
 * @author jaswine
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "gateway")
@ApiModel(value = "路由实体",description = "路由实体")
public class Router extends BasePojo {

	/** 路由id */
	private String routeId;
	/** 路由id */
	private String uri;
	/** 路由id */
	private String predicates;
	/** 路由id */
	private String filters;
	/** 路由id */
	private Long orders;
	/** 路由id */
	private String description;
	/** 路由id */
	private Long status;

}
