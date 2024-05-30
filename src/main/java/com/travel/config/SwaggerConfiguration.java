package com.travel.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "여행 플래너 API 명세서",
	        description = "<h3>Travel API Reference for Developers</h3>Swagger를 이용한 VUE API<br>",
	        version = "v1",
	        contact = @Contact(
	            name = "jjh",
	            email = "jang_jaehu@naver.com",
	            url = "http://naver.com"
	        )
	    )
	)
@Configuration
public class SwaggerConfiguration {

	@Bean
	public GroupedOpenApi allApi() {
		return GroupedOpenApi.builder().group("travel-all").pathsToMatch("/**").build();
	}

	@Bean
	public GroupedOpenApi boardApi() {
		return GroupedOpenApi.builder().group("travel-board").pathsToMatch("/board/**").build();
	}
	
	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder().group("travel-user").pathsToMatch("/user/**").build();
	}
	
	@Bean
	public GroupedOpenApi mapApi() {
		return GroupedOpenApi.builder().group("travel-map").pathsToMatch("/map/**").build();
	}
	
}
