package com.jaswine.gateway.manage.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger UI
 *
 * @author jaswine
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		log.info("配置Swagger API");
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.jaswine.gateway.manage.rest"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("CLOUD-GATEWAY")
				.description("微服务-网关管理")
				.termsOfServiceUrl("https://jaswine.com")
				.version("1.0")
				.build();
	}
}
