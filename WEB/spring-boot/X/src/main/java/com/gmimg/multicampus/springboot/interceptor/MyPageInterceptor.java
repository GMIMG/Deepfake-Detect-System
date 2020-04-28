package com.gmimg.multicampus.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gmimg.multicampus.springboot.member.Member;

@Component
public class MyPageInterceptor extends HandlerInterceptorAdapter{
	
	//페이지에 도착하기 전에 intercept를 하여 적용
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception{
		
		HttpSession session = request.getSession();
		//페이지에 sessionMem 세션객체를 member 변수로 가져옴
		Member member  = (Member) session.getAttribute("sessionMem");
		
		if (member == null) {
			//가져온 세션객체가 없다면 로그인을 하도록 로그인 페이지로 유도
			response.sendRedirect("/member/loginForm");
			return false;
		}
		
		return true;
	}
}
