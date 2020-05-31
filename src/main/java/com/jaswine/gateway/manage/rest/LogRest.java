package com.jaswine.gateway.manage.rest;

import com.jaswine.bean.base.dto.DTO;
import com.jaswine.gateway.manage.bean.cd.LogCD;
import com.jaswine.gateway.manage.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : Jaswine
 * @date : 2020-04-20 16:45
 **/
@RestController
@RequestMapping(value = "/log")
@Api(value = "操作日志API",tags = "操作日志API")
public class LogRest {

	@Resource
	private LogService logService;

	@PostMapping(value = "/all")
	public DTO getLogInfoPage(@RequestBody LogCD cd){
		return logService.getLogInfoPage(cd);
	}

}
