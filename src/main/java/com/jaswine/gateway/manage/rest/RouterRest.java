package com.jaswine.gateway.manage.rest;

import com.jaswine.bean.base.dto.DTO;
import com.jaswine.gateway.manage.bean.cd.RouterCd;
import com.jaswine.gateway.manage.bean.pojo.Router;
import com.jaswine.gateway.manage.service.RouterService;
import com.jaswine.log.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 路由REST
 *
 * @author jaswine
 */
@RestController
@RequestMapping(value = "/route")
@Api(value = "路由API",tags = "路由API")
public class RouterRest {

	@Resource
	private RouterService routerService;


	/**
	 * 初始化路由表
	 * @return 路由表
	 * @throws IOException 异常
	 * @throws SQLException 异常
	 */
	@GetMapping(value = "init")
	@ApiOperation(value = "初始化路由&日志表")
	public DTO init() throws IOException, SQLException {
		return routerService.init();
	}

	/**
	 * 插入路由信息
	 * @param router 路由
	 * @return dto
	 */
	@Log(description = "新增路由信息")
	@PostMapping
	@ApiOperation(value = "新增路由信息")
	public DTO insertRouter(@RequestBody Router router){
		return routerService.insertRouter(router);
	}

	/**
	 * 删除路由信息
	 * @param rid 路由id
	 * @return dto
	 */
	@Log(description = "删除路由信息")
	@DeleteMapping(value = "{rid}")
	@ApiOperation(value = "删除路由信息")
	public DTO deleteRouter(@PathVariable Long rid){
		return routerService.deleteRouter(rid);
	}

	/**
	 * 删除路由组
	 * @param rids 路由组
	 * @return dto
	 */
	@Log(description = "删除路由信息[多个]")
	@DeleteMapping(value = "group")
	@ApiOperation(value = "删除路由信息[多个]")
	public DTO deleteRouterByGroup(@RequestBody Long[] rids){
		return routerService.deleteRouterByGroup(rids);
	}

	/**
	 * 更新路由信息
	 * @param rid 路由id
	 * @param router 路由信息
	 * @return dto
	 */
	@Log(description = "更新路由信息")
	@PutMapping(value = "{rid}")
	@ApiOperation(value = "更新路由信息")
	public DTO updateRouter(@PathVariable Long rid,
	                        @RequestBody Router router){
		return routerService.updateRouter(rid,router);
	}

	/**
	 * 依据路由id获得路由信息
	 * @param rid 路由id
	 * @return dto
	 */
	@GetMapping(value = "{rid}")
	@ApiOperation(value = "获得路由详细信息")
	public DTO getRouterById(@PathVariable Long rid){
		return routerService.getRouterById(rid);
	}

	/**
	 * 获得所有路由信息
	 * @param cd 条件
	 * @return dto
	 */
	@PostMapping(value = "all")
	@ApiOperation(value = "获得路由详细信息-分页")
	public DTO getAllRouter(@RequestBody RouterCd cd){
		return routerService.getAllRouter(cd);
	}
}
