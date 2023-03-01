package com.itheima.reggie.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.itheima.reggie.common.JacksonObjectMapper;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	/**
	 * 拓展消息转换器
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setObjectMapper(new JacksonObjectMapper());
		converters.add(0, messageConverter);
	}

	/**
	 * 静态资源配置
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
		registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
	}

}
