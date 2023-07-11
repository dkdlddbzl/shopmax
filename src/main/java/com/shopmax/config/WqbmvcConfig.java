package com.shopmax.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//파일을 읽어올 경로를 설정
@Configuration
public class WqbmvcConfig implements WebMvcConfigurer {
	
	String uploadPath = "file:///C:/shop/"; // 업로드 경로
	
	
	//웹 으라우저에서 URL이 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어온다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
		.addResourceLocations(uploadPath);
	}

}
