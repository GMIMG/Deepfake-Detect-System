package com.gmimg.multicampus.springboot.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	//로그인을 하지 않고 mypage로 들어갈 수 없게 mypage로의 이동은 MyPageInterceptor()클래스를 거쳐가게 설정
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyPageInterceptor())
                .addPathPatterns("/mypage/**");
    }

}
