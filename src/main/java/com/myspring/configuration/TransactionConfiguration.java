package com.myspring.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.myspring")
public class TransactionConfiguration extends WebMvcConfigurerAdapter {

	/**
	 * 1. Single instance across app
	 * 2. Helps in unit testing
	 * 
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * Configure the Swagger UI resources 
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");
	 
	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}