package com.jaswine.gateway.manage.bean;

import lombok.Getter;

/**
 * 网关回复对象
 *
 * @author : Jaswine
 * @date : 2020-07-11 19:02
 **/
public enum GatewayRtnEnum {

	/** 路由状态没有变化 */
	ERROR_ROUTE_STATUS_NO_CHANGE(1000,"路由状态没有变化");


	@Getter
	private Integer status;
	@Getter
	private String msg;

	private GatewayRtnEnum(Integer status, String msg) {
		this.status = status;
		this.msg = msg;
	}

}
