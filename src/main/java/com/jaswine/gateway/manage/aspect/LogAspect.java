package com.jaswine.gateway.manage.aspect;

import com.lanswon.commons.core.log.Log;
import com.lanswon.commons.web.custom.dto.DTO;
import com.lanswon.commons.web.custom.dto.SimpleRtnDTO;
import com.lanswon.commons.web.custom.rtn.CustomRtnEnum;
import com.lanswon.commons.web.net.IpAddressUtil;
import com.lanswon.gateway.manage.bean.pojo.OperationLogPojo;
import com.lanswon.gateway.manage.mapper.OperationLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 日志切点
 *
 * @author : Jaswine
 * @date : 2020-04-16 22:52
 **/
@Aspect
@Component
public class LogAspect {

	@Resource
	private OperationLogMapper operationLogMapper;

	@Pointcut("@annotation(com.lanswon.commons.core.log.Log)")
	public void pointCut(){}


	@Around("pointCut()")
	public DTO around(ProceedingJoinPoint point){
		long start = System.currentTimeMillis();

		DTO proceed = null;
		try {
			proceed = (DTO)point.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		long time = System.currentTimeMillis() - start;
		boolean flag = saveLog(point, start, time);

		if (!flag){
			return new SimpleRtnDTO(CustomRtnEnum.ERROR_CUSTOM.getStatus(),"请求失败");
		}

		return proceed;
	}

	private boolean saveLog(ProceedingJoinPoint point,Long start,Long time){
		OperationLogPojo logPojo = new OperationLogPojo();

		MethodSignature signature =  (MethodSignature)point.getSignature();
		Method method = signature.getMethod();

		Log annotation = method.getAnnotation(Log.class);
		if (annotation != null){
			// 操作描述
			logPojo.setOperation(annotation.description());
		}
		// 方法名
		logPojo.setMethod(point.getTarget().getClass().getName() + "---" + signature.getName());

		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
		String args = Arrays.toString(point.getArgs());
		// 参数
		assert parameterNames != null;
		logPojo.setParams(String.join(",",parameterNames) + "---" + args);
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		// HTTP方法
		logPojo.setMethodType(request.getMethod());
		// 操作人ip
		logPojo.setIp(IpAddressUtil.getIpAddress(request));
		// 操作时间
		logPojo.setStartTime(new Date(start));
		// 方法时长
		logPojo.setTime(time);

		if (request.getHeader("userName") == null){
			return false;
		}
		logPojo.setUsername(request.getHeader("userName"));

		operationLogMapper.insert(logPojo);
		return true;
	}


}
