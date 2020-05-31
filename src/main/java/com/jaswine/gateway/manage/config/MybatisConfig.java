package com.jaswine.gateway.manage.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置
 * <P>
 *     配置扫描包
 * </P>
 * @author Jaswine
 */
@Configuration
@MapperScan(basePackages = "com.jaswine.gateway.manage.mapper")
@Slf4j
public class MybatisConfig {

	/**
	 * SQL执行效率插件
	 * @return 效率插件
	 */
	@Bean
	public PerformanceMonitorInterceptor performanceMonitorInterceptor(){
		log.info("配置SQL执行效率插件");
		return new PerformanceMonitorInterceptor();
	}


	/**
	 * 分页插件
	 * @return 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor(){
		log.info("配置分页插件");
		return new PaginationInterceptor();
	}

}
