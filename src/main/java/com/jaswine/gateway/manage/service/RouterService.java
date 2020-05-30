package com.jaswine.gateway.manage.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanswon.commons.bean.message.Constant;
import com.lanswon.commons.web.custom.dto.DTO;
import com.lanswon.commons.web.custom.dto.DataRtnDTO;
import com.lanswon.commons.web.custom.dto.SimpleRtnDTO;
import com.lanswon.commons.web.custom.rtn.CustomRtnEnum;
import com.lanswon.gateway.manage.bean.cd.RouterCd;
import com.lanswon.gateway.manage.bean.pojo.Router;
import com.lanswon.gateway.manage.mapper.RouterMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Router Service
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
	private RocketMQTemplate rocketMQTemplate;


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
	public DTO insertRouter(Router router) {
		log.info("新增路由信息");
		log.info(rocketMQTemplate.getProducer().getNamesrvAddr());
		if (routerMapper.insert(router) == 0){
			log.error("新增失败");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_BAD_SQL.getStatus(), CustomRtnEnum.ERROR_BAD_SQL.getMsg());
		}

		log.info("新增成功,发送消息");
		rocketMQTemplate.convertAndSend(Constant.MSG_TOPIC_ADD_GATEWAY,router);
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

	public DTO updateRouter(Long rid, Router router) {
		log.info("更新路由信息--{}",rid);
		router.setId(rid);
		router.setCreatedBy(null);
		router.setCreatedTime(null);
		if (routerMapper.updateById(router) == 0){
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
		IPage<Router> routeList = routerMapper.getAllRouter(new Page<>(cd.getPage(), cd.getLimit()), cd);

		if (routeList.getRecords().isEmpty()){
			log.error("结果为空");
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_EMPTY_RESULT.getStatus(), CustomRtnEnum.ERROR_EMPTY_RESULT.getMsg());
		}

		log.info("查询成功");
		return new DataRtnDTO<>(CustomRtnEnum.SUCCESS.getStatus(), CustomRtnEnum.SUCCESS.getMsg(),routeList);
	}


	private boolean checkDatatableExist(){
		log.info("检查 路由表 & 日志表  是否存在");
		if (routerMapper.checkTableExist(Constant.GATEWAY_TABLE_NAME) == null){
			log.error("路由表不存在");
			return false;
		}

		if (routerMapper.checkTableExist(Constant.LOG_TABLE_NAME) == null){
			log.error("日志表不存在");
			return false;
		}

		return true;
	}
}
