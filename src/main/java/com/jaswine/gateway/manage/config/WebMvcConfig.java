package com.jaswine.gateway.manage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.List;


/**
 * Web MVC配置
 * <P>
 *     1.配置CORS
 *
 * </P>
 * @author Jaswine
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * CORS配置
     * @return filter
     */
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        log.info("配置CORS");
        return new CorsFilter(source);

    }


	/**
     * Json转换,解决Long类型前端Js精度丢失的问题
     * @param converters 转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("配置JSON转换器");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


        // Object和Json转换对象
        ObjectMapper mapper = new ObjectMapper();
        // ObjectMapper的Module
        SimpleModule module = new SimpleModule();

        module.addSerializer(BigInteger.class, ToStringSerializer.instance);
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(long.class, ToStringSerializer.instance);

        mapper.registerModule(module);
        converter.setObjectMapper(mapper);

        converters.add(0,converter);
    }
}
