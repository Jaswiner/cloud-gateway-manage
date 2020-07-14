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
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "route")
@ApiModel(value = "路由实体",description = "路由实体")
public class Route extends BasePojo {

	/** 路由id */
	private String routeId;
	/** 指向 */
	private String uri;
	/** 谓语动词 */
	private String predicates;
	/** 过滤器 */
	private String filters;
	/** 顺序 */
	private Integer orders;
	/** 描述 */
	private String description;
	/** 状态 */
	private Integer status;

}
