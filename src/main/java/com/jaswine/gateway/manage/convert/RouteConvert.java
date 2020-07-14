package com.jaswine.gateway.manage.convert;

import com.jaswine.bean.route.RedisFilterDefinition;
import com.jaswine.bean.route.RedisPredicateDefinition;
import com.jaswine.bean.route.RedisRouteDefinition;
import com.jaswine.gateway.manage.bean.pojo.Route;
import com.jaswine.tools.json.JsonUtil;


/**
 * @author : Jaswine
 * @date : 2020-07-11 19:12
 **/
public class RouteConvert {



	public static RedisRouteDefinition dbRouteConvert(Route route){
		return new RedisRouteDefinition().toBuilder()
							.routeId(route.getRouteId())
							.uri(route.getUri())
							.predicates(JsonUtil.json2List(route.getPredicates(), RedisPredicateDefinition.class))
							.filters(JsonUtil.json2List(route.getFilters(), RedisFilterDefinition.class))
							.description(route.getDescription())
							.orders(route.getOrders())
							.status(route.getStatus())
							.build();
	}

}
