package com.jaswine.gateway.manage.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaswine.bean.base.dto.DTO;
import com.jaswine.bean.base.dto.DataRtnDTO;
import com.jaswine.bean.base.dto.SimpleRtnDTO;
import com.jaswine.bean.base.rtn.CustomRtnEnum;
import com.jaswine.bean.message.MessageConstant;
import com.jaswine.bean.redis.RedisKeysConstant;
import com.jaswine.bean.route.RedisRouteDefinition;
import com.jaswine.gateway.manage.bean.Constant;
import com.jaswine.gateway.manage.bean.GatewayRtnEnum;
import com.jaswine.gateway.manage.bean.cd.RouterCd;
import com.jaswine.gateway.manage.bean.dto.RouteDTO;
import com.jaswine.gateway.manage.bean.pojo.Route;
import com.jaswine.gateway.manage.convert.RouteConvert;
import com.jaswine.gateway.manage.mapper.RouterMapper;
import com.jaswine.tools.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

/**
 * Route Service
 *
 * @author jaswine
 */
@Service
@Slf4j
public class RouterService {

	@Resource
	private DruidDataSource dataSource;
	@Resource
	private RouterMapper routerMapper;
	@Resource
	private RabbitTemplate rabbitTemplate;
	@Resource
	private RedisTemplate<String,Object> redisTemplate;


	/**
	 * 初始化数据库
	 *
	 * gateway配置数据库-gateway
	 * gateway操作日志数据库-operation_log
	 *
	 * @return Res
	 * @throws SQLException SQL异常
	 * @throws IOException IO异常
	 */
	public DTO init() throws SQLException, IOException {

		if (checkDatatableExist()){
			log.error("路由表 & 日志表都已存在,不必初始化");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_CUSTOM.getStatus(),"路由表 & 日志表都已存在,不必初始化");
		}

		log.info("初始化路由信息");

		new ScriptRunner(dataSource.getConnection()).runScript(new InputStreamReader((new ClassPathResource("db"+File.separator+"db.sql").getInputStream())));


		log.info(CustomRtnEnum.SUCCESS.toString());
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(),CustomRtnEnum.SUCCESS.getMsg());
	}


	@Transactional(rollbackFor = Exception.class)
	public DTO insertRouter(RouteDTO routeDTO) {
		log.info("新增路由信息");

		Route route = new Route().toBuilder()
				.routeId(routeDTO.getRouteId())
				.uri(routeDTO.getUri())
				.predicates(JsonUtil.obj2String(routeDTO.getPredicates()))
				.filters(JsonUtil.obj2String(routeDTO.getFilters()))
				.description(routeDTO.getDescription())
				.orders(routeDTO.getOrders())
				.status(routeDTO.getStatus())
				.build();
		if (routerMapper.insert(route) == 0){
			log.error("新增失败");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_BAD_SQL.getStatus(), CustomRtnEnum.ERROR_BAD_SQL.getMsg());
		}

		log.info("将数据插到Redis中:{}", route.toString());
		RedisRouteDefinition redisRouteDefinition = new RedisRouteDefinition();
		BeanUtils.copyProperties(routeDTO,redisRouteDefinition);
		redisTemplate.opsForHash().put(RedisKeysConstant.KEY_GATEWAY_ROUTES,
										route.getRouteId(),
										JsonUtil.obj2String(redisRouteDefinition));

		log.info("新增成功,发送消息到队列:{}",MessageConstant.MSG_TOPIC_ADD_GATEWAY);
		rabbitTemplate.convertAndSend(MessageConstant.MSG_TOPIC_ADD_GATEWAY, JsonUtil.obj2String(redisRouteDefinition));
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg());

	}

	public DTO deleteRouter(Long rid) {
		log.info("删除路由信息--{}",rid);
		if (routerMapper.deleteById(rid) == 0){
			log.error("删除失败");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_BAD_SQL.getStatus(), CustomRtnEnum.ERROR_BAD_SQL.getMsg());
		}

		log.info("删除成功");
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg());
	}

	public DTO deleteRouterByGroup(Long[] rids) {
		Arrays.asList(rids).forEach(rid ->
			log.info("批量删除路由信息--{}", rid)
		);
		if (routerMapper.deleteBatchIds(new ArrayList<>(Arrays.asList(rids))) == 0){
			log.error("删除失败");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_BAD_SQL.getStatus(), CustomRtnEnum.ERROR_BAD_SQL.getMsg());
		}

		log.info("删除成功");
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg());
	}

	public DTO updateRouter(Long rid, Route route) {
		log.info("更新路由信息--{}",rid);
		route.setId(rid);
		route.setCreatedBy(null);
		route.setCreatedTime(null);
		if (routerMapper.updateById(route) == 0){
			log.error("更新失败");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_BAD_SQL.getStatus(), CustomRtnEnum.ERROR_BAD_SQL.getMsg());
		}

		log.info("更新成功");
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg());
	}

	public DTO getRouterById(Long rid) {
		log.info("获得路由信息--{}",rid);
		if (routerMapper.selectById(rid) == null){
			log.error("没有该路由信息");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_CUSTOM.getStatus(), "没有该路由信息");
		}

		log.info("查找成功成功");
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg());
	}

	public DTO getAllRouter(RouterCd cd) {
		log.info("按照条件查找路由信息--{}",cd.toString());
		IPage<Route> routeList = routerMapper.getAllRouter(new Page<>(cd.getPage(), cd.getLimit()), cd);

		if (routeList.getRecords().isEmpty()){
			log.error("结果为空");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_EMPTY_RESULT.getStatus(), CustomRtnEnum.ERROR_EMPTY_RESULT.getMsg());
		}

		log.info("查询成功");
		return new DataRtnDTO<>(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg(),routeList);
	}


	private boolean checkDatatableExist(){
		log.info("检查 路由表 & 日志表  是否存在");
		if (routerMapper.checkTableExist(MessageConstant.GATEWAY_TABLE_NAME) == null){
			log.error("路由表不存在");
			return false;
		}

		if (routerMapper.checkTableExist(MessageConstant.LOG_TABLE_NAME) == null){
			log.error("日志表不存在");
			return false;
		}

		return true;
	}

	public DTO initRoutesInRedis() {
		log.info("初始化Redis中的路由信息");
		redisTemplate.delete(RedisKeysConstant.KEY_GATEWAY_ROUTES);

		Set<Route> routes = routerMapper.getAllRouteWithOutPage();
		List<Long> ids = new ArrayList<>();
		routes.forEach(route -> {
			redisTemplate.opsForHash().putAll(RedisKeysConstant.KEY_GATEWAY_ROUTES+":"+ route.getId(), BeanMap.create(route));
			ids.add(route.getId());
		});
		rabbitTemplate.convertAndSend(MessageConstant.MSG_TOPIC_INIT_GATEWAY,ids);

		log.info(CustomRtnEnum.SUCCESS.toString()+"-->{}",redisTemplate.opsForList().leftPop(RedisKeysConstant.KEY_GATEWAY_ROUTES));
		return new DataRtnDTO<>(CustomRtnEnum.SUCCESS.getStatus(),CustomRtnEnum.SUCCESS.getMsg(),routes);
	}

	@Transactional(rollbackFor = Exception.class)
	public DTO updateRouteStatus(Long id, Integer status) {
		log.info("修改ID为 : {}的Route状态为 : {}",id,status);

		// 1.查询该Route的详情
		Route route = routerMapper.selectById(id);

		// 2.判断DB中状态和目标状态
		if (route.getStatus().equals(status)){
			log.error("目标状态和当前状态相同,无需修改");
			return new SimpleRtnDTO(GatewayRtnEnum.ERROR_ROUTE_STATUS_NO_CHANGE.getStatus(),GatewayRtnEnum.ERROR_ROUTE_STATUS_NO_CHANGE.getMsg());
		}

		log.debug("修改数据库");
		routerMapper.updateRouteStatusById(id,status);

		// 2.1.启用
		if (Constant.ROUTE_STATUS_ENABLE.equals(status)){
			log.info("启用Route : {}",id);

			String routeJson = JsonUtil.obj2String(RouteConvert.dbRouteConvert(route));

			log.debug("新增Redis");
			redisTemplate.opsForHash().put(RedisKeysConstant.KEY_GATEWAY_ROUTES,route.getRouteId(),routeJson);

			log.debug("发送消息");
			rabbitTemplate.convertAndSend(MessageConstant.MSG_TOPIC_ADD_GATEWAY,routeJson);
		}

		// 2.2.禁用
		if (Constant.ROUTE_STATUS_DISABLE.equals(status)){
			log.info("禁用Route : {}",id);

			log.debug("新增Redis");
			redisTemplate.opsForHash().delete(RedisKeysConstant.KEY_GATEWAY_ROUTES,route.getRouteId());

			log.debug("发送消息");
			rabbitTemplate.convertAndSend(MessageConstant.MSG_TOPIC_DELETE_GATEWAY,route.getRouteId());
		}

		log.info(CustomRtnEnum.SUCCESS.toString());
		return new SimpleRtnDTO(CustomRtnEnum.SUCCESS.getStatus(),CustomRtnEnum.SUCCESS.getMsg());
	}
}
