package com.gmimg.multicampus.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gmimg.multicampus.springboot.mapper.IMemMapper;

@Controller
public class MemberController {

	@Autowired
	IMemMapper memMapper;
	
	
	//회원가입
	@RequestMapping(value = "/member/registerForm", method = RequestMethod.GET)
	public String registerForm() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(
			@RequestParam("regMemId") String Id,
			@RequestParam("regMemPw") String Pw, ModelAndView mav) {
		
		memMapper.insertMem(Id, Pw);
		
		mav.addObject("regId", Id);
		mav.addObject("regPw", Pw);
		mav.setViewName("registerOk");
		
		//String map = memMapper.findAll("heee@email.com").getMemPw();
		
		return mav;
	}
	//로그인 
	@RequestMapping(value = "/member/loginForm")
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(ModelAndView mav,
			@RequestParam("loginMemId") String loginId,
			@RequestParam("loginMemPw") String loginPw) {
		return mav;
	}
}
