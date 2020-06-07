package com.jaswine.gateway.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaswine.bean.base.dto.DTO;
import com.jaswine.bean.base.dto.DataRtnDTO;
import com.jaswine.bean.base.rtn.CustomRtnEnum;
import com.jaswine.gateway.manage.bean.cd.LogCD;
import com.jaswine.gateway.manage.bean.pojo.OperationLogPojo;
import com.jaswine.gateway.manage.mapper.OperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 日志service
 *
 * @author : Jaswine
 * @date : 2020-04-20 21:15
 **/
@Service
@Slf4j
public class LogService {

	@Resource
	private OperationLogMapper operationLogMapper;

	public DTO getLogInfoPage(LogCD cd) {
		log.info("查看日志信息");

		IPage<OperationLogPojo> logPage = operationLogMapper.getOperationLogPage(new Page<>(cd.getPage(), cd.getLimit()), cd);

		return new DataRtnDTO<>(CustomRtnEnum.SUCCESS.getStatus(),CustomRtnEnum.SUCCESS.getMsg(),logPage);

	}

}
